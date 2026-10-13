package com.my.zyro.feature_console_rpc

import com.my.zyro.domain.model.Game
import com.my.zyro.domain.model.rpc.RpcConfig
import com.my.zyro.feature_rpc_base.Constants
import com.my.zyro.feature_rpc_base.Constants.PlatformOption
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal fun consoleConfig(game: Game, platform: PlatformOption, startedAt: Long): RpcConfig = RpcConfig(
    name = platform.displayName,
    details = game.game_title,
    timestampsStart = startedAt.toString(), status = "dnd", type = "0",
    largeImg = game.large_image.orEmpty(), smallImg = platform.iconUrl,
    platform = platform.activityPlatform.orEmpty()
)

internal fun restoreConsoleConfig(payload: String): String {
    if (payload.isBlank()) return ""
    return try {
        val codec = Json { ignoreUnknownKeys = true }
        val saved = codec.decodeFromString<RpcConfig>(payload)
        val choice = Constants.CONSOLE_PLATFORMS.firstOrNull { it.displayName == saved.name }
        codec.encodeToString(saved.copy(
            platform = choice?.activityPlatform ?: saved.platform.takeIf {
                it in setOf("ps4", "ps5", "xbox", "desktop", "android", "ios", "samsung", "embedded")
            }.orEmpty(),
            smallImg = if (saved.smallImg.startsWith("https://i.imgur.com/") && saved.smallImg.endsWith("_icon.png")) "" else saved.smallImg
        ))
    } catch (_: Exception) { "" }
}
