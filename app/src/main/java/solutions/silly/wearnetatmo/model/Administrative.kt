package solutions.silly.wearnetatmo.model

import com.squareup.moshi.Json

data class Administrative(
    val country: String?,
    @Json(name = "reg_locale")
    val regLocale: String?,
    val lang: String?,
    val unit: Long?,
    val windunit: Long?,
    val pressureunit: Long?,
    @Json(name = "feel_like_algo")
    val feelLikeAlgo: Long?,
)