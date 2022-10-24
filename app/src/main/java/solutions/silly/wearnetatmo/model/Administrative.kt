package solutions.silly.wearnetatmo.model

import com.squareup.moshi.Json

data class Administrative(
    val country: String?,
    @field:Json(name = "reg_locale")
    val regLocale: String?,
    val lang: String?,
    val unit: Int?, // 0 -> metric system, 1 -> imperial system
    val windunit: Int?, // 0 -> kph, 1 -> mph, 2 -> ms, 3 -> beaufort, 4 -> knot
    val pressureunit: Int?, // 0 -> mbar, 1 -> inHg, 2 -> mmHg
    @field:Json(name = "feel_like_algo")
    val feelLikeAlgo: Int?, // algorithm used to compute feel like temperature, 0 -> humidex, 1 -> heat-index
)