package solutions.silly.wearnetatmo.model

import com.squareup.moshi.Json

data class NetatmoToken(
    @Json(name = "access_token")
    val accessToken: String?,
    @Json(name = "expires_in")
    val expiresIn: Long?,
    @Json(name = "refresh_token")
    val refreshToken: String?
)