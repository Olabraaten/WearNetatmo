package solutions.silly.wearnetatmo.model

import com.squareup.moshi.Json

data class DeviceDashboardData(
    @field:Json(name = "time_utc")
    val timeUtc: Long?,
    @field:Json(name = "Temperature")
    val temperature: Double?,
    @field:Json(name = "CO2")
    val co2: Int?,
    @field:Json(name = "Humidity")
    val humidity: Int?,
    @field:Json(name = "Noise")
    val noise: Int?,
    @field:Json(name = "Pressure")
    val pressure: Double?,
    @field:Json(name = "AbsolutePressure")
    val absolutePressure: Double?,
    @field:Json(name = "min_temp")
    val minTemp: Double?,
    @field:Json(name = "max_temp")
    val maxTemp: Double?,
    @field:Json(name = "date_max_temp")
    val dateMaxTemp: Long?,
    @field:Json(name = "date_min_temp")
    val dateMinTemp: Long?,
    @field:Json(name = "temp_trend")
    val tempTrend: String?,
    @field:Json(name = "pressure_trend")
    val pressureTrend: String?,
)