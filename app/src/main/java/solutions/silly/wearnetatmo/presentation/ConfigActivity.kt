package solutions.silly.wearnetatmo.presentation

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.ListSubHeader
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.TextToggleButton
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import androidx.wear.compose.material3.lazy.transformedHeight
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices
import com.google.android.horologist.compose.layout.ColumnItemType
import com.google.android.horologist.compose.layout.rememberResponsiveColumnPadding
import dagger.hilt.android.AndroidEntryPoint
import solutions.silly.wearnetatmo.R
import solutions.silly.wearnetatmo.model.Device
import solutions.silly.wearnetatmo.model.DeviceDashboardData
import solutions.silly.wearnetatmo.model.Place
import solutions.silly.wearnetatmo.presentation.theme.WearNetatmoTheme

@AndroidEntryPoint
class ConfigActivity : ComponentActivity() {

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
        val columnState = rememberTransformingLazyColumnState()
        val contentPadding = rememberResponsiveColumnPadding(
            first = ColumnItemType.ListHeader,
            last = ColumnItemType.Button,
        )
        val transformationSpec = rememberTransformationSpec()
        ScreenScaffold(
            scrollState = columnState,
            contentPadding = contentPadding
        ) { contentPadding ->
            TransformingLazyColumn(
                state = columnState,
                contentPadding = contentPadding
            ) {
                item {
                    ListHeader(
                        modifier = Modifier
                            .fillMaxWidth()
                            .transformedHeight(this, transformationSpec),
                        transformation = SurfaceTransformation(transformationSpec)
                    ) {
                        Text(text = "Netatmo")
                    }
                }
                item {
                    if (!uiState.isLoggedIn) {
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .transformedHeight(this, transformationSpec),
                            transformation = SurfaceTransformation(transformationSpec),
                            label = {
                                Text(text = stringResource(id = R.string.log_in_button))
                            },
                            secondaryLabel = {
                                Text(text = stringResource(id = R.string.log_in_button_description))
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
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .transformedHeight(this, transformationSpec),
                            transformation = SurfaceTransformation(transformationSpec),
                            label = {
                                Text(text = stringResource(id = R.string.log_out_button))
                            },
                            onClick = onAuthClick
                        )
                    }
                }
                if (uiState.stations.isNotEmpty()) {
                    item {
                        ListSubHeader(
                            modifier = Modifier
                                .fillMaxWidth()
                                .transformedHeight(this, transformationSpec),
                            transformation = SurfaceTransformation(transformationSpec)
                        ) {
                            Text("Stations")
                        }
                    }
                    items(
                        count = uiState.stations.size,
                        key = { index ->
                            uiState.stations[index].id.orEmpty()
                        }
                    ) { index ->
                        val device = uiState.stations[index]
                        TextToggleButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .transformedHeight(this, transformationSpec),
                            checked = uiState.selectedStation == device.id,
                            onCheckedChange = { isChecked ->
                                if (isChecked) {
                                    device.id?.let { id ->
                                        onSelectStationClick(id)
                                    }
                                }
                            }
                        ) {
                            Text(
                                text = device.homeName.orEmpty(),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .transformedHeight(this, transformationSpec),
                        transformation = SurfaceTransformation(transformationSpec),
                        onClick = onExitClick
                    ) {
                        Text(text = stringResource(id = R.string.done_button))
                    }
                }
            }
        }
    }
}

@WearPreviewDevices
@Composable
fun DefaultPreview() {
    ConfigScreen(
        uiState = ConfigUiState(),
        onAuthClick = {},
        onExitClick = {},
        onSelectStationClick = {}
    )
}

@WearPreviewDevices
@Composable
fun LoggedInPreview() {
    ConfigScreen(
        uiState = ConfigUiState(
            isLoggedIn = true,
            stations = listOf(
                Device(
                    id = "",
                    stationName = "Station 1",
                    dateSetup = 0,
                    lastSetup = 0,
                    type = "TODO()",
                    lastStatusStore = 0,
                    moduleName = "Module 1",
                    firmware = 0,
                    lastUpgrade = 0,
                    wifiStatus = 0,
                    reachable = true,
                    co2Calibrating = false,
                    dataType = emptyList(),
                    place = Place(
                        altitude = 0,
                        city = "",
                        country = "",
                        timezone = "",
                        location = emptyList()
                    ),
                    homeId = "",
                    homeName = "Home 1",
                    dashboardData = DeviceDashboardData(
                        timeUtc = 0,
                        temperature = 0.0,
                        co2 = 0,
                        humidity = 0,
                        noise = 0,
                        pressure = 0.0,
                        absolutePressure = 0.0,
                        minTemp = 0.0,
                        maxTemp = 0.0,
                        dateMaxTemp = 0,
                        dateMinTemp = 0,
                        tempTrend = "",
                        pressureTrend = ""
                    ),
                    modules = listOf(),
                    readOnly = true
                )
            )
        ),
        onAuthClick = {},
        onExitClick = {},
        onSelectStationClick = {}
    )
}