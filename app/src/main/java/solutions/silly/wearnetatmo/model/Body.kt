package solutions.silly.wearnetatmo.model

import kotlinx.serialization.Serializable

@Serializable
data class Body(
    val devices: List<Device>? = null,
    val user: User? = null,
)