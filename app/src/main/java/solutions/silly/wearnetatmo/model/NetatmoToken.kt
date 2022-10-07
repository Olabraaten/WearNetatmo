package solutions.silly.wearnetatmo.model

import com.squareup.moshi.Json

data class NetatmoToken(
    @field:Json(name = "access_token") val accessToken: String?,
    @field:Json(name = "expires_in") val expiresIn: Long?,
    @field:Json(name = "refresh_token") val refreshToken: String?
)