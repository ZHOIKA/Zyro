package com.my.zyro.feature_console_rpc

import com.my.zyro.domain.model.Game
import com.my.zyro.domain.model.rpc.RpcConfig
import com.my.zyro.feature_rpc_base.Constants
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.*
import org.junit.Test

class ConsoleConfigTest {
    @Test fun xboxUsesProtocolPlatformAndKeepsGameDetails() {
        val config = consoleConfig(Game("Xbox", "", null, "Halo"), Constants.CONSOLE_PLATFORMS.first { it.displayName == "Xbox Series X" }, 123L)
        assertEquals("xbox", config.platform)
        assertEquals("Halo", config.details)
        assertEquals("123", config.timestampsStart)
        assertEquals("", config.largeImg)
        assertEquals("", config.smallImg)
    }
    @Test fun savedLegacyPlayStationConfigIsMigrated() {
        val legacy = RpcConfig(name = "PlayStation 5", platform = "PlayStation 5", details = "Game", smallImg = "https://i.imgur.com/ps5_icon.png")
        val restored = Json.decodeFromString<RpcConfig>(restoreConsoleConfig(Json.encodeToString(legacy)))
        assertEquals("ps5", restored.platform)
        assertEquals("Game", restored.details)
        assertEquals("", restored.smallImg)
    }
    @Test fun malformedSavedConfigRequiresANewSelection() {
        assertEquals("", restoreConsoleConfig("broken json"))
    }
    @Test fun switchDoesNotInventUnsupportedPlatformIdentifier() {
        val config = consoleConfig(Game("Nintendo", "", null, "Zelda"), Constants.CONSOLE_PLATFORMS.first { it.displayName == "Nintendo Switch" }, 1L)
        assertEquals("Nintendo Switch", config.name)
        assertEquals("", config.platform)
    }
}
