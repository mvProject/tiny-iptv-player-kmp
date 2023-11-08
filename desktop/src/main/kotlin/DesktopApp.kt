import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication
import com.mvproject.tinyiptv.MainRes
import com.mvproject.tinyiptv.TinyIptvApp
import com.mvproject.tinyiptv.di.initKoin

fun main() {

    initKoin()

    return singleWindowApplication(
        exitProcessOnExit = true,
        title = MainRes.string.app_name,
        icon = BitmapPainter(useResource("drawable/no_channel_logo.png", ::loadImageBitmap)),
        state = WindowState(
            position = WindowPosition.Aligned(Alignment.Center),
            width = 1400.dp,
            height = 900.dp
        )
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            TinyIptvApp()
        }
    }
}