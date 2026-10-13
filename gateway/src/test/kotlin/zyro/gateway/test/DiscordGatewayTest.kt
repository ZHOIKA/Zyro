/*
 *
 *  ******************************************************************
 *  *  * Copyright (C) 2022
 *  *  * DiscordGatewayTest.kt is part of Zyro
 *  *  *  and can not be copied and/or distributed without the express
 *  *  * permission of zk
 *  *  *****************************************************************
 *
 *
 */

package zyro.gateway.test

import com.my.zyro.domain.interfaces.Logger
import zyro.gateway.DiscordWebSocketImpl
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.Assume.assumeTrue
import org.junit.Assert.assertTrue
import org.junit.Assert.assertSame
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.awaitCancellation
import kotlin.time.Duration.Companion.seconds

class DiscordGatewayTest {
    @Test
    fun `Closing cancels child jobs and keeps a reusable stable scope`() = runBlocking {
        val gateway = DiscordWebSocketImpl("test-token")
        val lifecycle = gateway.coroutineContext[Job]
        assertSame(lifecycle, gateway.coroutineContext[Job])
        val child = gateway.launch { awaitCancellation() }
        gateway.close()
        child.join()
        assertTrue(child.isCancelled)
        assertTrue(requireNotNull(lifecycle).isActive)
        assertTrue(!gateway.isWebSocketConnected())
    }

    @Test
    fun `Check Gateway Connection`() = runBlocking {
        val logger = object: Logger  {
            override fun clear() {

            }
            override fun i(tag: String, event: String) {
                println("[$tag]\t\t\t: $event")
            }
            override fun e(tag: String, event: String) {
                println("[$tag]\t\t\t: $event")
            }
            override fun d(tag: String, event: String) {
                println("[$tag]\t\t\t: $event")
            }
            override fun w(tag: String, event: String) {
                println("[$tag]\t\t\t: $event")
            }

        }
        val token = System.getenv("DISCORD_TOKEN")
        assumeTrue("Live test requires DISCORD_TOKEN", !token.isNullOrBlank())
        val gateway = DiscordWebSocketImpl(requireNotNull(token), logger)
        try {
            gateway.connect()
            delay(2.seconds)
            assertTrue(gateway.isWebSocketConnected())
        } finally {
            gateway.close()
        }
    }
}