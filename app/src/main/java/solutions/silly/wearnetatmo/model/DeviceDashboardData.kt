package solutions.silly.wearnetatmo.model

import com.squareup.moshi.Json

data class DeviceDashboardData(
    @Json(name = "time_utc")
    val timeUtc: Long?,
    @Json(name = "Temperature")
    val temperature: Double?,
    @Json(name = "CO2")
    val co2: Long?,
    @Json(name = "Humidity")
    val humidity: Long?,
    @Json(name = "Noise")
    val noise: Long?,
    @Json(name = "Pressure")
    val pressure: Double?,
    @Json(name = "AbsolutePressure")
    val absolutePressure: Double?,
    @Json(name = "min_temp")
    val minTemp: Double?,
    @Json(name = "max_temp")
    val maxTemp: Double?,
    @Json(name = "date_max_temp")
    val dateMaxTemp: Long?,
    @Json(name = "date_min_temp")
    val dateMinTemp: Long?,
    @Json(name = "temp_trend")
    val tempTrend: String?,
    @Json(name = "pressure_trend")
    val pressureTrend: String?,
)