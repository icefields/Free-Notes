package luci.sixsixsix.homemessageshare.presentation.main.components

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import luci.sixsixsix.homemessageshare.presentation.common.TextWithOverline

@Composable
fun CollectionInfoTextWithTitle(@StringRes title: Int, subtitle: String?) {
    subtitle?.let {
        if (it.isNotBlank()) {
            TextWithOverline(title = title, subtitle = it)
        }
    }
}
