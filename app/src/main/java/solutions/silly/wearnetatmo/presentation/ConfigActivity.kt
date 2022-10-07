package solutions.silly.wearnetatmo.presentation

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.RadioButton
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.ToggleChip
import dagger.hilt.android.AndroidEntryPoint
import solutions.silly.wearnetatmo.R
import solutions.silly.wearnetatmo.presentation.theme.WearNetatmoTheme

@AndroidEntryPoint
class ConfigActivity : ComponentActivity() {

    @OptIn(ExperimentalLifecycleComposeApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val configViewModel: ConfigViewModel = viewModel()
            val uiState by configViewModel.uiState.collectAsStateWithLifecycle()
            ConfigScreen(
                uiState = uiState,
                onAuthClick = {
                    if (uiState.isLoggedIn) {
                        configViewModel.unAuthenticate()
                    } else {
                        configViewModel.authenticate()
                    }
                },
                onExitClick = {
                    setResult(RESULT_OK)
                    finish()
                },
                onSelectStationClick = { id ->
                    configViewModel.selectWeatherStation(id)
                }
            )
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
}

@Composable
fun ConfigScreen(
    uiState: ConfigUiState,
    onAuthClick: () -> Unit,
    onExitClick: () -> Unit,
    onSelectStationClick: (id: String) -> Unit,
) {
    WearNetatmoTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!uiState.isLoggedIn) {
                Chip(
                    label = {
                        Text(text = "Log in")
                    },
                    secondaryLabel = {
                        Text(text = "On phone")
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_send_to_mobile),
                            contentDescription = null
                        )
                    },
                    onClick = onAuthClick
                )
            } else {
                Chip(
                    label = {
                        Text(text = "Log out")
                    },
                    onClick = onAuthClick
                )
            }
            if (uiState.stations.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                uiState.stations.forEach { device ->
                    ToggleChip(
                        checked = uiState.selectedStation == device.id,
                        onCheckedChange = { isChecked ->
                            if (isChecked) {
                                device.id?.let { id ->
                                    onSelectStationClick(id)
                                }
                            }
                        },
                        label = {
                            Text(
                                text = device.homeName.orEmpty(),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        toggleControl = {
                            RadioButton(selected = uiState.selectedStation == device.id)
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onExitClick
            ) {
                Text(text = "Done")
            }
        }
    }
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    ConfigScreen(
        uiState = ConfigUiState(),
        onAuthClick = {},
        onExitClick = {},
        onSelectStationClick = {}
    )
}