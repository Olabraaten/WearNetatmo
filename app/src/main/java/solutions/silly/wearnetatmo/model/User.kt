package solutions.silly.wearnetatmo.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val mail: String? = null,
    val administrative: Administrative? = null,
)