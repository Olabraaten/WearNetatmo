package solutions.silly.wearnetatmo.model

import com.squareup.moshi.Json

data class ModuleDashboardData(
    @field:Json(name = "time_utc")
    val timeUtc: Long?,
    @field:Json(name = "Temperature")
    val temperature: Double?,
    @field:Json(name = "Humidity")
    val humidity: Long?,
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
    @field:Json(name = "Rain")
    val rain: Long?,
    @field:Json(name = "sum_rain_1")
    val sumRain1: Long?,
    @field:Json(name = "sum_rain_24")
    val sumRain24: Double?,
    @field:Json(name = "WindStrength")
    val windStrength: Long?,
    @field:Json(name = "WindAngle")
    val windAngle: Long?,
    @field:Json(name = "GustStrength")
    val gustStrength: Long?,
    @field:Json(name = "GustAngle")
    val gustAngle: Long?,
    @field:Json(name = "max_wind_str")
    val maxWindStr: Long?,
    @field:Json(name = "max_wind_angle")
    val maxWindAngle: Long?,
    @field:Json(name = "date_max_wind_str")
    val dateMaxWindStr: Long?,
)