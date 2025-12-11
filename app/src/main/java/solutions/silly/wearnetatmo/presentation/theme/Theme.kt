package solutions.silly.wearnetatmo.presentation.theme

import androidx.compose.runtime.Composable
import androidx.wear.compose.material3.MaterialTheme

@Composable
fun WearNetatmoTheme(
        content: @Composable () -> Unit
) {
    MaterialTheme(
            colorScheme = wearColorPalette,
            typography = Typography,
            // For shapes, we generally recommend using the default Material Wear shapes which are
            // optimized for round and non-round devices.
            content = content
    )
}