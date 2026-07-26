package zyro.gateway.entities.presence

import kotlinx.serialization.Serializable

/**
 * Activity Flags bitfield as defined in Discord Gateway API v10
 * See: https://discord.com/developers/docs/topics/gateway-events#activity-object-activity-flags
 */
@Serializable
data class ActivityFlags(
    val value: Int = 0
) {
    companion object {
        const val INSTANCE = 1 shl 0       // 1
        const val JOIN = 1 shl 1            // 2
        const val SPECTATE = 1 shl 2        // 4 (deprecated)
        const val JOIN_REQUEST = 1 shl 3    // 8
        const val SYNC = 1 shl 4            // 16
        const val PLAY = 1 shl 5            // 32
        const val PARTY_PRIVACY_FRIENDS = 1 shl 6   // 64
        const val PARTY_PRIVACY_VOICE_CHANNEL = 1 shl 7 // 128
        const val EMBEDDED = 1 shl 8        // 256
    }

    fun hasFlag(flag: Int): Boolean = (value and flag) == flag

    fun withFlag(flag: Int): ActivityFlags = ActivityFlags(value or flag)

    fun withoutFlag(flag: Int): ActivityFlags = ActivityFlags(value and flag.inv())
}

