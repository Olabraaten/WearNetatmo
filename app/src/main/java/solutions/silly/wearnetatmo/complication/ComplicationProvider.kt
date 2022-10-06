package solutions.silly.wearnetatmo.complication

import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.PlainComplicationText
import androidx.wear.watchface.complications.data.ShortTextComplicationData
import androidx.wear.watchface.complications.datasource.ComplicationDataSourceService
import androidx.wear.watchface.complications.datasource.ComplicationRequest

class ComplicationProvider : ComplicationDataSourceService() {

    override fun getPreviewData(type: ComplicationType): ComplicationData? {
        val title = PlainComplicationText.Builder(
            text = "24°C In"
        ).build()
        val text = PlainComplicationText.Builder(
            text = "12°C Out"
        ).build()
        return ShortTextComplicationData.Builder(
            text = text,
            contentDescription = text
        ).setTitle(title)
            .build()
    }

    override fun onComplicationRequest(
        request: ComplicationRequest,
        listener: ComplicationRequestListener
    ) {
        val title = PlainComplicationText.Builder(
            text = "24°C In"
        ).build()
        val text = PlainComplicationText.Builder(
            text = "12°C Out"
        ).build()
        val data = ShortTextComplicationData.Builder(
            text = text,
            contentDescription = text
        ).setTitle(title)
            .build()
        listener.onComplicationData(data)
    }
}