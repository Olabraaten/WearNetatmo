package solutions.silly.wearnetatmo.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Module(
    @SerialName("_id")
    val id: String? = null,
    val type: String? = null,
    @SerialName("module_name")
    val moduleName: String? = null,
    @SerialName("last_setup")
    val lastSetup: Long? = null,
    @SerialName("data_type")
    val dataType: List<String>? = null,
    @SerialName("battery_percent")
    val batteryPercent: Long? = null,
    val reachable: Boolean? = null,
    val firmware: Long? = null,
    @SerialName("last_message")
    val lastMessage: Long? = null,
    @SerialName("last_seen")
    val lastSeen: Long? = null,
    @SerialName("rf_status")
    val rfStatus: Int? = null,
    @SerialName("battery_vp")
    val batteryVp: Int? = null,
    @SerialName("dashboard_data")
    val dashboardData: ModuleDashboardData? = null,
)