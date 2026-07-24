package zyro.gateway

import com.my.zyro.domain.interfaces.Logger
import com.my.zyro.domain.interfaces.NoOpLogger
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import zyro.gateway.entities.Heartbeat
import zyro.gateway.entities.Identify.Companion.toIdentifyPayload
import zyro.gateway.entities.OutgoingPayload
import zyro.gateway.entities.Payload
import zyro.gateway.entities.PayloadData
import zyro.gateway.entities.Ready
import zyro.gateway.entities.Resume
import zyro.gateway.entities.op.OpCode
import zyro.gateway.entities.op.OpCode.*
import zyro.gateway.entities.presence.Presence
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration.Companion.milliseconds

/**
 * Zyro Discord WebSocket Implementation
 * Completely reimplemented WebSocket client for Discord Gateway v10 protocol
 * Manages connection lifecycle, heartbeat, and presence updates independently
 */
open class DiscordWebSocketImpl(
    private val authToken: String,
    private val logger: Logger = NoOpLogger
) : DiscordWebSocket {
    
    // ============== Gateway Configuration ==============
    
    private companion object {
        const val DISCORD_GATEWAY_URL = "wss://gateway.discord.gg/?v=10&encoding=json"
        const val RECONNECT_DELAY_MS = 200L
        const val SOCKET_CHECK_INTERVAL_MS = 10L
    }
    
    // ============== Internal State ==============
    
    private var wsSession: DefaultClientWebSocketSession? = null
    private var lastSequenceNumber = 0
    private var currentSessionId: String? = null
    private var resumeUrl: String? = null
    private var heartbeatIntervalMs = 0L
    private var activeHeartbeatJob: Job? = null
    private var isConnectedToAccount = false
    
    private val httpClient: HttpClient = HttpClient {
        install(WebSockets)
    }
    
    private val jsonCodec = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.Default

    // ============== Connection Lifecycle ==============

    /**
     * Initiates WebSocket connection to Discord Gateway
     * Handles initial connection and message processing loop
     */
    override suspend fun connect() {
        launch {
            try {
                logger.i("GatewayImpl", "Initiating WebSocket connection")
                
                val targetUrl = resumeUrl ?: DISCORD_GATEWAY_URL
                wsSession = httpClient.webSocketSession(targetUrl)

                // Main message processing loop
                wsSession!!.incoming.receiveAsFlow().collect { frame ->
                    when (frame) {
                        is Frame.Text -> processIncomingMessage(frame.readText())
                        else -> {} // Ignore other frame types
                    }
                }
                
                processConnectionClosed()
                
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                logger.e("GatewayImpl", "Connection error: ${e.message ?: "Unknown"}")
                close()
            }
        }
    }

    /**
     * Handles graceful connection closure and reconnection logic
     */
    private suspend fun processConnectionClosed() {
        activeHeartbeatJob?.cancel()
        isConnectedToAccount = false
        
        val closeReason = wsSession?.closeReason?.await()
        val closeCode = closeReason?.code?.toInt()
        val canReconnect = closeCode == 4000
        
        logger.w(
            "GatewayImpl",
            "Connection closed. Code: $closeCode, Reason: ${closeReason?.message}, Reconnect: $canReconnect"
        )
        
        if (canReconnect) {
            delay(RECONNECT_DELAY_MS.milliseconds)
            connect()
        } else {
            close()
        }
    }

    /**
     * Processes incoming gateway payloads and dispatches to appropriate handler
     */
    private suspend fun processIncomingMessage(payloadJson: String) {
        try {
            val gatewayPayload = jsonCodec.decodeFromString<Payload>(payloadJson)
            
            logger.d(
                "GatewayImpl",
                "Received opcode:${gatewayPayload.op}, seq:${gatewayPayload.s}, event:${gatewayPayload.t}"
            )

            // Update sequence tracking
            gatewayPayload.s?.let { seq ->
                lastSequenceNumber = seq
            }

            // Route to appropriate handler based on opcode
            when (gatewayPayload.op) {
                DISPATCH -> handleDispatchEvent(payloadJson, gatewayPayload)
                HEARTBEAT -> sendHeartbeatAck()
                RECONNECT -> initiateReconnection()
                INVALID_SESSION -> handleSessionInvalid()
                HELLO -> handleHelloPayload(payloadJson)
                else -> {} // Unhandled opcodes ignored
            }
        } catch (e: Exception) {
            logger.e("GatewayImpl", "Error processing message: ${e.message}")
        }
    }

    /**
     * Handles DISPATCH events (READY, RESUMED, etc.)
     */
    private fun handleDispatchEvent(payloadJson: String, payload: Payload) {
        when (payload.t.toString()) {
            "READY" -> {
                val readyData = decodePayloadData<Ready>(payloadJson) ?: return
                currentSessionId = readyData.sessionId
                resumeUrl = readyData.resumeGatewayUrl + "/?v=10&encoding=json"
                logger.i("GatewayImpl", "Session ready. Resume URL: $resumeUrl")
                logger.i("GatewayImpl", "Session ID: $currentSessionId")
                isConnectedToAccount = true
            }
            "RESUMED" -> {
                logger.i("GatewayImpl", "Session successfully resumed")
                isConnectedToAccount = true
            }
            else -> {} // Other dispatch events ignored
        }
    }

    /**
     * Handles invalid session by re-identifying
     */
    private suspend fun handleSessionInvalid() {
        logger.i("GatewayImpl", "Invalid session detected, re-identifying")
        delay(150)
        sendIdentifyPayload()
    }

    /**
     * Handles HELLO opcode to extract heartbeat interval and proceed with auth flow
     */
    private suspend fun handleHelloPayload(payloadJson: String) {
        // Determine if we should resume or identify
        val shouldResume = lastSequenceNumber > 0 && !currentSessionId.isNullOrBlank()
        
        if (shouldResume) {
            sendResumePayload()
        } else {
            sendIdentifyPayload()
        }

        // Extract and set heartbeat interval
        val heartbeatData = decodePayloadData<Heartbeat>(payloadJson)
        heartbeatIntervalMs = heartbeatData?.heartbeatInterval ?: return
        logger.i("GatewayImpl", "Heartbeat interval set: ${heartbeatIntervalMs}ms")
        
        startHeartbeatLoop(heartbeatIntervalMs)
    }

    // ============== Payload Decoding ==============

    /**
     * Decodes typed payload data from gateway JSON
     */
    private inline fun <reified T> decodePayloadData(jsonString: String): T? {
        return try {
            jsonCodec.decodeFromString<PayloadData<T>>(jsonString).d
        } catch (e: Exception) {
            logger.e("GatewayImpl", "Failed to decode payload data: ${e.message}")
            null
        }
    }

    // ============== Outgoing Messages ==============

    /**
     * Sends heartbeat acknowledgment with current sequence
     */
    private suspend fun sendHeartbeatAck() {
        logger.i("GatewayImpl", "Sending heartbeat ACK with seq: $lastSequenceNumber")
        transmitPayload(
            opcode = HEARTBEAT,
            data = if (lastSequenceNumber == 0) "null" else lastSequenceNumber.toString()
        )
    }

    /**
     * Sends identify payload to establish new session
     */
    private suspend fun sendIdentifyPayload() {
        logger.i("GatewayImpl", "Sending IDENTIFY payload")
        transmitPayload(
            opcode = IDENTIFY,
            data = authToken.toIdentifyPayload()
        )
    }

    /**
     * Sends resume payload to restore existing session
     */
    private suspend fun sendResumePayload() {
        logger.i("GatewayImpl", "Sending RESUME payload")
        transmitPayload(
            opcode = RESUME,
            data = Resume(
                seq = lastSequenceNumber,
                sessionId = currentSessionId,
                token = authToken
            )
        )
    }

    /**
     * Initiates reconnection by closing socket with reconnect code
     */
    private suspend fun initiateReconnection() {
        logger.i("GatewayImpl", "Reconnection requested by gateway")
        wsSession?.close(
            CloseReason(
                code = 4000,
                message = "Zyro reconnection initiated"
            )
        )
    }

    /**
     * Generic payload transmission
     */
    private suspend inline fun <reified T> transmitPayload(opcode: OpCode, data: T?) {
        if (wsSession?.isActive == true) {
            val outgoingPayload = jsonCodec.encodeToString(
                OutgoingPayload(
                    op = opcode,
                    d = data
                )
            )
            wsSession?.send(Frame.Text(outgoingPayload))
        }
    }

    // ============== Heartbeat Management ==============

    /**
     * Starts periodic heartbeat job
     */
    private fun startHeartbeatLoop(intervalMs: Long) {
        activeHeartbeatJob?.cancel()
        activeHeartbeatJob = launch {
            while (isActive) {
                sendHeartbeatAck()
                delay(intervalMs)
            }
        }
    }

    /**
     * Checks if socket is fully connected to Discord account
     */
    private fun isFullyConnected(): Boolean {
        return isConnectedToAccount && wsSession?.isActive == true
    }

    /**
     * Checks if WebSocket session is active
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun isWebSocketConnected(): Boolean {
        return wsSession?.incoming != null && wsSession?.outgoing?.isClosedForSend == false
    }

    // ============== Presence Updates ==============

    /**
     * Sends presence/activity update to Discord
     */
    override suspend fun sendActivity(presence: Presence) {
        // Wait for account connection before sending
        while (!isFullyConnected()) {
            delay(SOCKET_CHECK_INTERVAL_MS.milliseconds)
        }
        
        logger.i("GatewayImpl", "Transmitting presence update")
        transmitPayload(
            opcode = PRESENCE_UPDATE,
            data = presence
        )
    }

    // ============== Resource Cleanup ==============

    /**
     * Gracefully closes WebSocket connection and cancels all jobs
     */
    override fun close() {
        activeHeartbeatJob?.cancel()
        activeHeartbeatJob = null
        this.cancel()
        
        resumeUrl = null
        currentSessionId = null
        isConnectedToAccount = false
        
        runBlocking {
            wsSession?.close()
            logger.e("GatewayImpl", "WebSocket connection terminated")
        }
    }
}
