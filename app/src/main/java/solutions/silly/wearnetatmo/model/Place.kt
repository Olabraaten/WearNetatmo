package solutions.silly.wearnetatmo.model

import kotlinx.serialization.Serializable

@Serializable
data class Place(
    val altitude: Int? = null,
    val city: String? = null,
    val country: String? = null,
    val timezone: String? = null,
    val location: List<Double>? = null,
)