package solutions.silly.wearnetatmo.model

import com.squareup.moshi.Json

data class Module(
    @field:Json(name = "_id")
    val id: String?,
    val type: String?,
    @field:Json(name = "module_name")
    val moduleName: String?,
    @field:Json(name = "last_setup")
    val lastSetup: Long?,
    @field:Json(name = "data_type")
    val dataType: List<String>?,
    @field:Json(name = "battery_percent")
    val batteryPercent: Long?,
    val reachable: Boolean?,
    val firmware: Long?,
    @field:Json(name = "last_message")
    val lastMessage: Long?,
    @field:Json(name = "last_seen")
    val lastSeen: Long?,
    @field:Json(name = "rf_status")
    val rfStatus: Long?,
    @field:Json(name = "battery_vp")
    val batteryVp: Long?,
    @field:Json(name = "dashboard_data")
    val dashboardData: ModuleDashboardData?,
)