package solutions.silly.wearnetatmo.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StationsData(
    val body: Body? = null,
    val status: String? = null,
    @SerialName("time_exec")
    val timeExec: Double? = null,
    @SerialName("time_server")
    val timeServer: Double? = null,
)