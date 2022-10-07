package solutions.silly.wearnetatmo.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import solutions.silly.wearnetatmo.ACCESS_TOKEN_KEY
import solutions.silly.wearnetatmo.CACHE_DURATION
import solutions.silly.wearnetatmo.EXPIRES_TOKEN_KEY
import solutions.silly.wearnetatmo.REFRESH_TOKEN_KEY
import solutions.silly.wearnetatmo.SELECTED_STATION_KEY
import solutions.silly.wearnetatmo.SecretConstants
import solutions.silly.wearnetatmo.SecretConstants.NETATMO_REDIRECT_URI
import solutions.silly.wearnetatmo.api.NetatmoService
import solutions.silly.wearnetatmo.model.NetatmoToken
import solutions.silly.wearnetatmo.model.StationsData
import timber.log.Timber
import java.io.IOException
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetatmoRepository @Inject constructor(
    private val netatmoService: NetatmoService,
    private val sharedPreferences: SharedPreferences
) {
    private var stationsDataCache: StationsData? = null
    private var stationsDataCacheExpiresTimestamp = 0L

    suspend fun getToken(code: String): Result<NetatmoToken> {
        try {
            val netatmoToken = netatmoService.getToken(
                grantType = "authorization_code",
                clientId = SecretConstants.NETATMO_CLIENT_ID,
                clientSecret = SecretConstants.NETATMO_CLIENT_SECRET,
                code = code,
                redirectUri = NETATMO_REDIRECT_URI,
                scope = "read_station"
            )
            saveToken(netatmoToken)
            return Result.success(netatmoToken)
        } catch (e: Exception) {
            Timber.e(e)
        }
        return Result.failure(IOException("Authorization failed"))
    }

    suspend fun removeToken() {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit {
                remove(ACCESS_TOKEN_KEY)
                remove(REFRESH_TOKEN_KEY)
                remove(EXPIRES_TOKEN_KEY)
            }
        }
        stationsDataCache = null
        stationsDataCacheExpiresTimestamp = 0L
    }

    suspend fun getStationsData(): Result<StationsData> {
        if (stationsDataCacheExpiresTimestamp < Date().time) {
            try {
                val accessToken = getFreshAccessToken()
                val stationsData = netatmoService.getStationsData(
                    bearerToken = "Bearer $accessToken"
                )
                stationsDataCache = stationsData
                stationsDataCacheExpiresTimestamp = Date().time + CACHE_DURATION
                return Result.success(stationsData)
            } catch (e: Exception) {
                Timber.e(e)
            }
        } else {
            stationsDataCache?.let { stationsData ->
                return Result.success(stationsData)
            }
        }
        return Result.failure(IOException("Fetching stations data failed"))
    }

    fun getSelectedStationId(): String? {
        return sharedPreferences.getString(SELECTED_STATION_KEY, null)
    }

    suspend fun setSelectedStationId(id: String) {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit {
                putString(SELECTED_STATION_KEY, id)
            }
        }
    }

    private suspend fun getFreshAccessToken(): String? {
        val expires = sharedPreferences.getLong(EXPIRES_TOKEN_KEY, 0)
        if (Date().time < expires) {
            Timber.d("Using stored access token")
            return sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
        }
        // TODO Make sure refresh token exists
        try {
            val netatmoToken = netatmoService.refreshToken(
                grantType = "refresh_token",
                refreshToken = sharedPreferences.getString(REFRESH_TOKEN_KEY, "") ?: "",
                clientId = SecretConstants.NETATMO_CLIENT_ID,
                clientSecret = SecretConstants.NETATMO_CLIENT_SECRET
            )
            saveToken(netatmoToken)
            return netatmoToken.accessToken
        } catch (e: Exception) {
            Timber.e(e)
            removeToken()
        }
        return null
    }

    private suspend fun saveToken(netatmoToken: NetatmoToken) {
        Timber.d("Saving token $netatmoToken")
        withContext(Dispatchers.IO) {
            val expires = Date().time + ((netatmoToken.expiresIn ?: 0) * 1000)
            sharedPreferences.edit {
                putString(ACCESS_TOKEN_KEY, netatmoToken.accessToken)
                putString(REFRESH_TOKEN_KEY, netatmoToken.refreshToken)
                putLong(EXPIRES_TOKEN_KEY, expires)
            }
        }
    }
}