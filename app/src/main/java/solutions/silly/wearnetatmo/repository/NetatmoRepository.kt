package solutions.silly.wearnetatmo.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import solutions.silly.wearnetatmo.ACCESS_TOKEN_KEY
import solutions.silly.wearnetatmo.CACHE_DURATION
import solutions.silly.wearnetatmo.EXPIRES_TOKEN_KEY
import solutions.silly.wearnetatmo.REFRESH_TOKEN_KEY
import solutions.silly.wearnetatmo.SELECTED_STATION_KEY
import solutions.silly.wearnetatmo.SecretConstants
import solutions.silly.wearnetatmo.SecretConstants.NETATMO_REDIRECT_URI
import solutions.silly.wearnetatmo.api.NetatmoService
import solutions.silly.wearnetatmo.model.Device
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
                getFreshAccessToken()?.let { accessToken ->
                    val stationsData = netatmoService.getStationsData(
                        bearerToken = "Bearer $accessToken"
                    )
                    stationsDataCache = stationsData
                    stationsDataCacheExpiresTimestamp = Date().time + CACHE_DURATION
                    return Result.success(stationsData)
                }
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

    suspend fun getSelectedStation(): Device? {
        getSelectedStationId()?.let { stationId ->
            return getStationsData().getOrNull()?.body?.devices?.find { it.id == stationId }
        }
        return null
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
        if (Date().time < expires - 1000) {
            Timber.d("Using stored access token")
            return sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
        }
        if (!sharedPreferences.contains(REFRESH_TOKEN_KEY)) {
            Timber.e("No refresh token found")
            removeToken()
            return null
        }
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
            if (e is HttpException) {
                removeToken()
            }
        }
        return null
    }

    private suspend fun saveToken(netatmoToken: NetatmoToken) {
        Timber.d("Saving token $netatmoToken")
        withContext(Dispatchers.IO) {
            sharedPreferences.edit {
                netatmoToken.accessToken?.let { accessToken ->
                    putString(ACCESS_TOKEN_KEY, accessToken)
                }
                netatmoToken.refreshToken?.let { refreshToken ->
                    putString(REFRESH_TOKEN_KEY, refreshToken)
                }
                netatmoToken.expiresIn?.let { expiresIn ->
                    val expires = Date().time + (expiresIn * 1000)
                    putLong(EXPIRES_TOKEN_KEY, expires)
                }
            }
        }
    }
}