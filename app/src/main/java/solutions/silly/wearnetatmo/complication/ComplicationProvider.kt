package solutions.silly.wearnetatmo.complication

import android.graphics.drawable.Icon
import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.MonochromaticImage
import androidx.wear.watchface.complications.data.PlainComplicationText
import androidx.wear.watchface.complications.data.ShortTextComplicationData
import androidx.wear.watchface.complications.datasource.ComplicationRequest
import androidx.wear.watchface.complications.datasource.SuspendingComplicationDataSourceService
import dagger.hilt.android.AndroidEntryPoint
import solutions.silly.wearnetatmo.R
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
            text = "12.3"
        ).build()
        return ShortTextComplicationData.Builder(
            text = text,
            contentDescription = text
        ).setTitle(title)
            .build()
    }

    override suspend fun onComplicationRequest(request: ComplicationRequest): ComplicationData? {
        Timber.d("Updating complication data")
        return netatmoRepository.getSelectedStation()?.let { station ->
            val inside = station.dashboardData?.temperature.toString()
            val outside =
                station.modules?.find { it.type == "NAModule1" }?.dashboardData?.temperature.toString()

            val title = PlainComplicationText.Builder(
                text = inside
            ).build()
            val text = PlainComplicationText.Builder(
                text = outside
            ).build()
            ShortTextComplicationData.Builder(
                text = text,
                contentDescription = text
            ).setTitle(title)
                .setMonochromaticImage(
                    MonochromaticImage.Builder(
                        Icon.createWithResource(
                            this,
                            R.drawable.ic_thermostat
                        )
                    ).build()
                )
                .build()
        } ?: makeErrorText()
    }

    private fun makeErrorText(): ShortTextComplicationData {
        val title = PlainComplicationText.Builder(
            text = "Data"
        ).build()
        val text = PlainComplicationText.Builder(
            text = "Error"
        ).build()
        return ShortTextComplicationData.Builder(
            text = text,
            contentDescription = text
        ).setTitle(title)
            .build()
    }
}