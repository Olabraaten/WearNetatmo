package solutions.silly.wearnetatmo.model

data class Place(
    val altitude: Int?,
    val city: String?,
    val country: String?,
    val timezone: String?,
    val location: List<Double>?,
)