package solutions.silly.wearnetatmo.model

import com.squareup.moshi.Json

data class StationsData(
    val body: Body?,
    val status: String?,
    @Json(name = "time_exec")
    val timeExec: Double?,
    @Json(name = "time_server")
    val timeServer: Long?,
)