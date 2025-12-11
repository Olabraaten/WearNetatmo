package solutions.silly.wearnetatmo.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Administrative(
    val country: String? = null,
    @SerialName("reg_locale")
    val regLocale: String? = null,
    val lang: String? = null,
    val unit: Int? = null, // 0 -> metric system, 1 -> imperial system
    val windunit: Int? = null, // 0 -> kph, 1 -> mph, 2 -> ms, 3 -> beaufort, 4 -> knot
    val pressureunit: Int? = null, // 0 -> mbar, 1 -> inHg, 2 -> mmHg
    @SerialName("feel_like_algo")
    val feelLikeAlgo: Int? = null, // algorithm used to compute feel like temperature, 0 -> humidex, 1 -> heat-index
)