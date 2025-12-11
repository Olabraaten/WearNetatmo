package solutions.silly.wearnetatmo.complication

import android.app.PendingIntent
import android.content.ComponentName
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
            text = "24.7"
        ).build()
        val text = PlainComplicationText.Builder(
            text = "\uD83C\uDF24 12.3"
        ).build()
        return ShortTextComplicationData.Builder(
            text = text,
            contentDescription = text
        ).setTitle(title)
            .build()
    }

    override suspend fun onComplicationRequest(request: ComplicationRequest): ComplicationData {
        Timber.d("Updating complication data")
        val thisDataSource = ComponentName(this, javaClass)
        val complicationPendingIntent =
            ComplicationTapBroadcastReceiver.getToggleIntent(
                this,
                thisDataSource,
                request.complicationInstanceId
            )

        val stationResult = netatmoRepository.getSelectedStation()

        if (stationResult.isSuccess) {
            stationResult.getOrNull()?.let { station ->
                val inside = station.dashboardData?.temperature.toString()
                val outside =
                    station.modules?.find { it.type == "NAModule1" }?.dashboardData?.temperature.toString()

                val title = PlainComplicationText.Builder(
                    text = inside
                ).build()
                val text = PlainComplicationText.Builder(
                    text = "\uD83C\uDF24 $outside"
                ).build()
                return ShortTextComplicationData.Builder(
                    text = text,
                    contentDescription = text
                ).setTitle(title)
                    .setTapAction(complicationPendingIntent)
                    .build()
            }
        }

        return makeErrorText(stationResult.exceptionOrNull(), complicationPendingIntent)
    }

    private fun makeErrorText(
        exception: Throwable?,
        complicationPendingIntent: PendingIntent
    ): ShortTextComplicationData {
        val title = PlainComplicationText.Builder(
            text = when {
                exception is IllegalAccessException -> "Log"
                else -> "Data"
            }
        ).build()
        val text = PlainComplicationText.Builder(
            text = when {
                exception is IllegalAccessException -> "in"
                else -> "Error"
            }
        ).build()
        return ShortTextComplicationData.Builder(
            text = text,
            contentDescription = text
        ).setTitle(title)
            .setTapAction(complicationPendingIntent)
            .build()
    }
}