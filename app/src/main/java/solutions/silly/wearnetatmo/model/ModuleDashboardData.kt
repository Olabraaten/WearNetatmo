package solutions.silly.wearnetatmo.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ModuleDashboardData(
    @SerialName("time_utc")
    val timeUtc: Long? = null,
    @SerialName("Temperature")
    val temperature: Double? = null,
    @SerialName("Humidity")
    val humidity: Double? = null,
    @SerialName("min_temp")
    val minTemp: Double? = null,
    @SerialName("max_temp")
    val maxTemp: Double? = null,
    @SerialName("date_max_temp")
    val dateMaxTemp: Double? = null,
    @SerialName("date_min_temp")
    val dateMinTemp: Double? = null,
    @SerialName("temp_trend")
    val tempTrend: String? = null,
    @SerialName("Rain")
    val rain: Double? = null,
    @SerialName("sum_rain_1")
    val sumRain1: Double? = null,
    @SerialName("sum_rain_24")
    val sumRain24: Double? = null,
    @SerialName("WindStrength")
    val windStrength: Double? = null,
    @SerialName("WindAngle")
    val windAngle: Double? = null,
    @SerialName("GustStrength")
    val gustStrength: Double? = null,
    @SerialName("GustAngle")
    val gustAngle: Double? = null,
    @SerialName("max_wind_str")
    val maxWindStr: Double? = null,
    @SerialName("max_wind_angle")
    val maxWindAngle: Double? = null,
    @SerialName("date_max_wind_str")
    val dateMaxWindStr: Long? = null,
)