package solutions.silly.wearnetatmo.model

import com.squareup.moshi.Json

data class ModuleDashboardData(
    @Json(name = "time_utc")
    val timeUtc: Long?,
    @Json(name = "Temperature")
    val temperature: Double?,
    @Json(name = "Humidity")
    val humidity: Long?,
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
    @Json(name = "Rain")
    val rain: Long?,
    @Json(name = "sum_rain_1")
    val sumRain1: Long?,
    @Json(name = "sum_rain_24")
    val sumRain24: Double?,
    @Json(name = "WindStrength")
    val windStrength: Long?,
    @Json(name = "WindAngle")
    val windAngle: Long?,
    @Json(name = "GustStrength")
    val gustStrength: Long?,
    @Json(name = "GustAngle")
    val gustAngle: Long?,
    @Json(name = "max_wind_str")
    val maxWindStr: Long?,
    @Json(name = "max_wind_angle")
    val maxWindAngle: Long?,
    @Json(name = "date_max_wind_str")
    val dateMaxWindStr: Long?,
)