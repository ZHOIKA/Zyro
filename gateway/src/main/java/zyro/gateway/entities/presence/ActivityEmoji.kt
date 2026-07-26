package zyro.gateway.entities.presence

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Activity Emoji for Custom Status and Hang Status activities
 * See: https://discord.com/developers/docs/topics/gateway-events#activity-object-activity-emoji
 */
@Serializable
data class ActivityEmoji(
    @SerialName("name")
    val name: String? = null,
    @SerialName("id")
    val id: String? = null,
    @SerialName("animated")
    val animated: Boolean? = null
)

