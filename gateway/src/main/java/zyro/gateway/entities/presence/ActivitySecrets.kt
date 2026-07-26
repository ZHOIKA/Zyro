package zyro.gateway.entities.presence

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Activity Secrets for Rich Presence joining and spectating
 * See: https://discord.com/developers/docs/topics/gateway-events#activity-object-activity-secrets
 */
@Serializable
data class ActivitySecrets(
    @SerialName("join")
    val join: String? = null,
    @SerialName("spectate")
    val spectate: String? = null,
    @SerialName("match")
    val match: String? = null
)

