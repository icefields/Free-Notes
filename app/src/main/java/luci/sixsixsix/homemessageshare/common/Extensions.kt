package luci.sixsixsix.homemessageshare.common

import androidx.annotation.ColorInt
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

fun mockNotesCollection() = NotesCollection(
    UUID.randomUUID().toString(),
    LocalDateTime.now().toString(),
    LocalDateTime.now().toString(),
    "FF00FF",
    listOf(),
    UUID.randomUUID().toString(),
    "Dark"
)

fun getDateString() =
    LocalDateTime.now().toString().split(".")[0]