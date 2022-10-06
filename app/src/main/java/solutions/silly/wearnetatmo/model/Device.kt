package solutions.silly.wearnetatmo.model

import com.squareup.moshi.Json

data class Device(
    @Json(name = "_id")
    val id: String?,
    @Json(name = "station_name")
    val stationName: String?,
    @Json(name = "date_setup")
    val dateSetup: Long?,
    @Json(name = "last_setup")
    val lastSetup: Long?,
    val type: String?,
    @Json(name = "last_status_store")
    val lastStatusStore: Long?,
    @Json(name = "module_name")
    val moduleName: String?,
    val firmware: Long?,
    @Json(name = "last_upgrade")
    val lastUpgrade: Long?,
    @Json(name = "wifi_status")
    val wifiStatus: Long?,
    val reachable: Boolean?,
    @Json(name = "co2_calibrating")
    val co2Calibrating: Boolean?,
    @Json(name = "data_type")
    val dataType: List<String>?,
    val place: Place?,
    @Json(name = "home_id")
    val homeId: String?,
    @Json(name = "home_name")
    val homeName: String?,
    @Json(name = "dashboard_data")
    val dashboardData: DeviceDashboardData?,
    val modules: List<Module>?,
    @Json(name = "read_only")
    val readOnly: Boolean?,
)