package zyro.gateway.entities.presence

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Party(
    @SerialName("id")
    val id: String = "zyro",
    @SerialName("size")
    val size: List<Int> = listOf(0, 0)
)
