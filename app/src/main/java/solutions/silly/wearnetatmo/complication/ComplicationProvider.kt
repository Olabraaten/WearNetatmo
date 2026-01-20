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
import solutions.silly.wearnetatmo.work.NetatmoSyncWorker
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ComplicationProvider : SuspendingComplicationDataSourceService() {

    @Inject
    lateinit var netatmoRepository: NetatmoRepository

    // Example data for the complication picker on the watch
    override fun getPreviewData(type: ComplicationType): ComplicationData {
        return makeTemperatureText(
            inside = "24.7",
            outside = "12.3",
            complicationPendingIntent = null,
        )
    }

    override suspend fun onComplicationRequest(request: ComplicationRequest): ComplicationData {
        Timber.d("Updating complication content")

        // Enable tap to force update
        val thisDataSource = ComponentName(this, javaClass)
        val complicationPendingIntent = ComplicationTapBroadcastReceiver.getToggleIntent(
            this,
            thisDataSource,
            request.complicationInstanceId
        )

        // We have cached data, just return it immediately
        val cachedStation = netatmoRepository.getCachedSelectedStation()
        if (cachedStation != null) {
            return buildShortTextData(cachedStation, complicationPendingIntent)
        }

        // No selected station, config not completed
        if (netatmoRepository.getSelectedStationId() == null) {
            return makeErrorText(IllegalAccessException(), complicationPendingIntent)
        }

        // No cache, but station selected, enqueue fetch and show temporary text
        NetatmoSyncWorker.enqueueOneTime(this)
        return makeSyncText(complicationPendingIntent)
    }

    private fun buildShortTextData(
        station: solutions.silly.wearnetatmo.model.Device,
        complicationPendingIntent: PendingIntent
    ): ShortTextComplicationData {
        // Extract temps from station data
        val inside = station.dashboardData?.temperature.toString()
        val outside = station.modules?.find {
            it.type == "NAModule1"
        }?.dashboardData?.temperature.toString()

        return makeTemperatureText(
            inside = inside,
            outside = outside,
            complicationPendingIntent = complicationPendingIntent,
        )
    }

    private fun makeTemperatureText(
        inside: String,
        outside: String,
        complicationPendingIntent: PendingIntent?,
    ): ShortTextComplicationData {
        // Format the text
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

    // Not logged in or some other error
    private fun makeErrorText(
        exception: Throwable?,
        complicationPendingIntent: PendingIntent
    ): ShortTextComplicationData {
        val title = PlainComplicationText.Builder(
            text = when {
                exception is IllegalAccessException -> "Log"
                else -> "Data"
            },
        ).build()
        val text = PlainComplicationText.Builder(
            text = when {
                exception is IllegalAccessException -> "in"
                else -> "Error"
            },
        ).build()

        return ShortTextComplicationData.Builder(
            text = text,
            contentDescription = text,
        ).setTitle(title)
            .setTapAction(complicationPendingIntent)
            .build()
    }

    // Probably first time loading, just logged in, might not happen
    private fun makeSyncText(complicationPendingIntent: PendingIntent): ShortTextComplicationData {
        val title = PlainComplicationText.Builder(
            text = "Sync",
        ).build()
        val text = PlainComplicationText.Builder(
            text = "\u23F3",
        ).build()
        return ShortTextComplicationData.Builder(
            text = text,
            contentDescription = text,
        ).setTitle(title)
            .setTapAction(complicationPendingIntent)
            .build()
    }
}
