package solutions.silly.wearnetatmo.complication

import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.PlainComplicationText
import androidx.wear.watchface.complications.data.ShortTextComplicationData
import androidx.wear.watchface.complications.datasource.ComplicationRequest
import androidx.wear.watchface.complications.datasource.SuspendingComplicationDataSourceService
import dagger.hilt.android.AndroidEntryPoint
import solutions.silly.wearnetatmo.repository.NetatmoRepository
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ComplicationProvider : SuspendingComplicationDataSourceService() {

    @Inject
    lateinit var netatmoRepository: NetatmoRepository

    override fun getPreviewData(type: ComplicationType): ComplicationData {
        val title = PlainComplicationText.Builder(
            text = "24.7°C"
        ).build()
        val text = PlainComplicationText.Builder(
            text = "12.3°C"
        ).build()
        return ShortTextComplicationData.Builder(
            text = text,
            contentDescription = text
        ).setTitle(title)
            .build()
    }

    override suspend fun onComplicationRequest(request: ComplicationRequest): ComplicationData? {
        Timber.d("Updating complication data")
        try {
            netatmoRepository.getSelectedStation()?.let { station ->
                val inside = station.dashboardData?.temperature.toString()
                val outside =
                    station.modules?.find { it.type == "NAModule1" }?.dashboardData?.temperature.toString()

                val title = PlainComplicationText.Builder(
                    text = "$inside°C"
                ).build()
                val text = PlainComplicationText.Builder(
                    text = "$outside°C"
                ).build()
                return ShortTextComplicationData.Builder(
                    text = text,
                    contentDescription = text
                ).setTitle(title)
                    .build()
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
        return null
    }
}