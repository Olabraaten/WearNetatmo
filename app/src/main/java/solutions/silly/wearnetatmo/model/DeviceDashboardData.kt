package solutions.silly.wearnetatmo.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeviceDashboardData(
    @SerialName("time_utc")
    val timeUtc: Long? = null,
    @SerialName("Temperature")
    val temperature: Double? = null,
    @SerialName("CO2")
    val co2: Int? = null,
    @SerialName("Humidity")
    val humidity: Int? = null,
    @SerialName("Noise")
    val noise: Int? = null,
    @SerialName("Pressure")
    val pressure: Double? = null,
    @SerialName("AbsolutePressure")
    val absolutePressure: Double? = null,
    @SerialName("min_temp")
    val minTemp: Double? = null,
    @SerialName("max_temp")
    val maxTemp: Double? = null,
    @SerialName("date_max_temp")
    val dateMaxTemp: Long? = null,
    @SerialName("date_min_temp")
    val dateMinTemp: Long? = null,
    @SerialName("temp_trend")
    val tempTrend: String? = null,
    @SerialName("pressure_trend")
    val pressureTrend: String? = null,
)