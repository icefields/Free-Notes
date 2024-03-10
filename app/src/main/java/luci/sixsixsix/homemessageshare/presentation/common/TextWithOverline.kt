package luci.sixsixsix.homemessageshare.presentation.common

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import luci.sixsixsix.homemessageshare.R

@Composable
fun TextWithOverline(
    modifier: Modifier = Modifier.fillMaxWidth(),
    @StringRes title: Int,
    subtitle: String,
    subtitleTextSize: TextUnit = 16.sp,
    trailingIcon: ImageVector? = null,
    trailingIconContentDescription: String? = null,
    enabled: Boolean = true,
    onClick: () -> Unit = { }
) = TextWithOverline(
    modifier, stringResource(id = title), subtitle, subtitleTextSize,
    trailingIcon, trailingIconContentDescription, enabled, onClick
)

@Composable
fun TextWithOverline(
    modifier: Modifier = Modifier.fillMaxWidth(),
    title: String,
    subtitle: String,
    subtitleTextSize: TextUnit = 16.sp,
    trailingIcon: ImageVector? = null,
    trailingIconContentDescription: String? = null,
    enabled: Boolean = true,
    onClick: () -> Unit = { }
) {
    val alpha = if (!enabled) { 0.5f } else { 1f }

    Row(modifier = modifier
        .clickable { onClick() }
        .alpha(alpha)
        .padding(
            horizontal = dimensionResource(id = R.dimen.listItem_padding_horizontal),
            vertical = dimensionResource(id = R.dimen.listItem_padding_vertical)
        ),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = title,
                fontSize = 12.sp,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (subtitle.isNotBlank()) {
                Text(
                    text = subtitle,
                    fontSize = subtitleTextSize,
                    fontWeight = FontWeight.Normal
                )
            }
        }
        trailingIcon?.let {
            Icon(imageVector = it, contentDescription = trailingIconContentDescription)
        }
    }
}

@Composable
@Preview
fun TextWithOverlinePreview() {
    TextWithOverline(
        modifier = Modifier.background(Color.White),
        title = "R.string.app_name",
        subtitle = "R.string.settings_enableDebugLogging_title"
    )
}
