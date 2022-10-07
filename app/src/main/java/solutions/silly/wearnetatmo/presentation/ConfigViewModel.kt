package solutions.silly.wearnetatmo.presentation

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.wear.phone.interactions.authentication.CodeChallenge
import androidx.wear.phone.interactions.authentication.CodeVerifier
import androidx.wear.phone.interactions.authentication.OAuthRequest
import androidx.wear.phone.interactions.authentication.OAuthResponse
import androidx.wear.phone.interactions.authentication.RemoteAuthClient
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import solutions.silly.wearnetatmo.ACCESS_TOKEN_KEY
import solutions.silly.wearnetatmo.SecretConstants
import solutions.silly.wearnetatmo.model.Device
import solutions.silly.wearnetatmo.repository.NetatmoRepository
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class ConfigViewModel @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
    private val sharedPreferences: SharedPreferences,
    private val netatmoRepository: NetatmoRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ConfigUiState())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.update { currentState ->
            currentState.copy(
                isLoggedIn = sharedPreferences.contains(ACCESS_TOKEN_KEY)
            )
        }
    }

    fun authenticate() {
        viewModelScope.launch {
            val codeVerifier = CodeVerifier()
            val uri = Uri.Builder()
                .encodedPath("https://api.netatmo.com/oauth2/authorize")
                .appendQueryParameter("scope", "read_station")
                .appendQueryParameter("state", "random")
                .build()
            val request = OAuthRequest.Builder(applicationContext)
                .setAuthProviderUrl(uri)
                .setClientId(SecretConstants.NETATMO_CLIENT_ID)
                .setCodeChallenge(CodeChallenge(codeVerifier))
                .build()

            val responseUrl = retrieveAuthCode(request).getOrElse { throwable ->
                Timber.e(throwable)
                _uiState.update { currentState ->
                    currentState.copy(
                        authError = true
                    )
                }
                return@launch
            }

            val result = netatmoRepository.getToken(
                code = responseUrl.getQueryParameter("code") ?: ""
            )
            if (result.isFailure) {
                _uiState.update { currentState ->
                    currentState.copy(
                        authError = true
                    )
                }
            }
            if (result.isSuccess) {
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoggedIn = true
                    )
                }
                getWeatherStations()
            }
        }
    }

    fun unAuthenticate() {
        viewModelScope.launch {
            netatmoRepository.removeToken()
            _uiState.update {
                ConfigUiState()
            }
        }
    }

    fun selectWeatherStation(id: String) {
        viewModelScope.launch {
            netatmoRepository.setSelectedStationId(id)
            _uiState.update { currentState ->
                currentState.copy(
                    selectedStation = id
                )
            }
        }
    }

    private suspend fun getWeatherStations() {
        val result = netatmoRepository.getStationsData()
        var selectedId = netatmoRepository.getSelectedStationId()
        result.getOrNull()?.body?.devices?.let { devices ->
            if (selectedId == null && devices.isNotEmpty()) {
                devices.first().id?.let { firstId ->
                    selectedId = firstId
                    netatmoRepository.setSelectedStationId(firstId)
                }
            }
            _uiState.update { currentState ->
                currentState.copy(
                    stations = devices,
                    selectedStation = selectedId
                )
            }
        }
        result.exceptionOrNull()?.let {
            _uiState.update { currentState ->
                currentState.copy(
                    stationsError = true
                )
            }
        }
    }

    private suspend fun retrieveAuthCode(request: OAuthRequest): Result<Uri> {
        return suspendCoroutine { c ->
            RemoteAuthClient.create(applicationContext)
                .sendAuthorizationRequest(request, { command ->
                    command?.run()
                }, object : RemoteAuthClient.Callback() {
                    override fun onAuthorizationResponse(
                        request: OAuthRequest,
                        response: OAuthResponse
                    ) {
                        val url = response.responseUrl
                        if (url != null) {
                            Timber.d("Auth successful $url")
                            c.resume(Result.success(url))
                        } else {
                            Timber.e("Auth failed url is null")
                            c.resume(Result.failure(IOException("Authorization failed")))
                        }
                    }

                    override fun onAuthorizationError(request: OAuthRequest, errorCode: Int) {
                        Timber.e("Auth error: $errorCode")
                        c.resume(Result.failure(IOException("Authorization failed")))
                    }
                }
                )
        }
    }
}

data class ConfigUiState(
    val isLoggedIn: Boolean = false,
    val authError: Boolean = false,
    val stations: List<Device> = emptyList(),
    val stationsError: Boolean = false,
    val selectedStation: String? = null
)