package solutions.silly.wearnetatmo.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Device(
    @SerialName("_id")
    val id: String? = null,
    @SerialName("station_name")
    val stationName: String? = null,
    @SerialName("date_setup")
    val dateSetup: Long? = null,
    @SerialName("last_setup")
    val lastSetup: Long? = null,
    val type: String? = null,
    @SerialName("last_status_store")
    val lastStatusStore: Long? = null,
    @SerialName("module_name")
    val moduleName: String? = null,
    val firmware: Long? = null,
    @SerialName("last_upgrade")
    val lastUpgrade: Long? = null,
    @SerialName("wifi_status")
    val wifiStatus: Int? = null,
    val reachable: Boolean? = null,
    @SerialName("co2_calibrating")
    val co2Calibrating: Boolean? = null,
    @SerialName("data_type")
    val dataType: List<String>? = null,
    val place: Place? = null,
    @SerialName("home_id")
    val homeId: String? = null,
    @SerialName("home_name")
    val homeName: String? = null,
    @SerialName("dashboard_data")
    val dashboardData: DeviceDashboardData? = null,
    val modules: List<Module>? = null,
    @SerialName("read_only")
    val readOnly: Boolean? = null,
)