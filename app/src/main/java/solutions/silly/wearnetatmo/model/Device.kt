package solutions.silly.wearnetatmo.model

import com.squareup.moshi.Json

data class Device(
    @field:Json(name = "_id")
    val id: String?,
    @field:Json(name = "station_name")
    val stationName: String?,
    @field:Json(name = "date_setup")
    val dateSetup: Long?,
    @field:Json(name = "last_setup")
    val lastSetup: Long?,
    val type: String?,
    @field:Json(name = "last_status_store")
    val lastStatusStore: Long?,
    @field:Json(name = "module_name")
    val moduleName: String?,
    val firmware: Long?,
    @field:Json(name = "last_upgrade")
    val lastUpgrade: Long?,
    @field:Json(name = "wifi_status")
    val wifiStatus: Long?,
    val reachable: Boolean?,
    @field:Json(name = "co2_calibrating")
    val co2Calibrating: Boolean?,
    @field:Json(name = "data_type")
    val dataType: List<String>?,
    val place: Place?,
    @field:Json(name = "home_id")
    val homeId: String?,
    @field:Json(name = "home_name")
    val homeName: String?,
    @field:Json(name = "dashboard_data")
    val dashboardData: DeviceDashboardData?,
    val modules: List<Module>?,
    @field:Json(name = "read_only")
    val readOnly: Boolean?,
)