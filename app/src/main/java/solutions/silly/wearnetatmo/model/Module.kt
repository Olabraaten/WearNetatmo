package solutions.silly.wearnetatmo.model

import com.squareup.moshi.Json

data class Module(
    @Json(name = "_id")
    val id: String?,
    val type: String?,
    @Json(name = "module_name")
    val moduleName: String?,
    @Json(name = "last_setup")
    val lastSetup: Long?,
    @Json(name = "data_type")
    val dataType: List<String>?,
    @Json(name = "battery_percent")
    val batteryPercent: Long?,
    val reachable: Boolean?,
    val firmware: Long?,
    @Json(name = "last_message")
    val lastMessage: Long?,
    @Json(name = "last_seen")
    val lastSeen: Long?,
    @Json(name = "rf_status")
    val rfStatus: Long?,
    @Json(name = "battery_vp")
    val batteryVp: Long?,
    @Json(name = "dashboard_data")
    val dashboardData: ModuleDashboardData?,
)