package luci.sixsixsix.homemessageshare.common

import androidx.annotation.ColorInt
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.ColorUtils
import luci.sixsixsix.homemessageshare.domain.models.NotesCollection
import java.time.LocalDateTime
import java.util.UUID
import kotlin.math.absoluteValue

@ColorInt
fun String.toHslColor(saturation: Float = 0.5f, lightness: Float = 0.4f): Int {
    val hue = fold(0) { acc, char -> char.code + acc * 37 } % 360
    return ColorUtils.HSLToColor(floatArrayOf(hue.absoluteValue.toFloat(), saturation, lightness))
}

val String.colour
    get() = try {
        Color(android.graphics.Color.parseColor(this))
    } catch (e: Exception) {
        Color(android.graphics.Color.parseColor("#$this"))
    }

fun mockNotesCollection() = NotesCollection(
    UUID.randomUUID().toString(),
    getDateString(),
    getDateString(),
    "FF00FF",
    listOf(),
    UUID.randomUUID().toString(),
    "Dark"
)

fun getDateString() =
    LocalDateTime.now().toString().split(".")[0]