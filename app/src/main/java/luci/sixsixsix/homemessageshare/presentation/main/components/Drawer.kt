/**
 * Copyright (C) 2024  Antonio Tari
 *
 * This file is a part of Power Ampache 2
 * Ampache Android client application
 * @author Antonio Tari
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package luci.sixsixsix.homemessageshare.presentation.main.components

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import luci.sixsixsix.homemessageshare.R
import luci.sixsixsix.homemessageshare.common.mockNotesCollection
import luci.sixsixsix.homemessageshare.common.toHslColor
import luci.sixsixsix.homemessageshare.domain.models.NotesCollection
import luci.sixsixsix.homemessageshare.presentation.common.DonateButton
import luci.sixsixsix.homemessageshare.presentation.common.DonateButtonPreview
import java.time.LocalDateTime
import java.util.UUID

val drawerItems = listOf(
    MainContentMenuItem.Home,
    MainContentMenuItem.Library,
    MainContentMenuItem.Offline,
    MainContentMenuItem.Settings,
    MainContentMenuItem.About,
    MainContentMenuItem.Logout
)

@Composable
fun MainDrawer(
    user: NotesCollection,
    versionInfo: String,
    hideDonationButtons: Boolean,
    items: List<MainContentMenuItem> = drawerItems,
    onItemClick: (MainContentMenuItem) -> Unit,
    modifier: Modifier = Modifier,
    donateButton: @Composable () -> Unit = { DonateButton(
        isTransparent = true,
        modifier = Modifier.padding(horizontal = 16.dp)
    ) }
) {
    ModalDrawerSheet(
        modifier = modifier,
        //modifier = Modifier.fillMaxWidth(0.8f)
    ) {
        DrawerHeader(user)
        Divider()
        DrawerBody(
            modifier = Modifier.weight(1f),
            items = items,
            onItemClick = onItemClick
        )
        if (!hideDonationButtons) {
            donateButton()
        }
        Text(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            text = "v$versionInfo",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Light,
            color = MaterialTheme.colorScheme.onErrorContainer,
            fontSize = 12.sp
        )
    }
}

@Composable
fun DrawerHeader(user: NotesCollection) {
    // if dogmazic user show custom information

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        var showFullUserInfo by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clickable {
                    showFullUserInfo = !showFullUserInfo
                }
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {

                UserHead(id = user.id, firstName = user.serverAddress, lastName = user.dateCreated)

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 20.dp)
                ) {
                    Text(
                        text = user.id,
                        fontSize = 18.sp,
                        maxLines = 1,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight.Normal
                    )
                    Text(
                        text = user.serverAddress,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp,
                        maxLines = 1,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Light
                    )
                }
            }
            Spacer(modifier = Modifier.height(11.dp))
            AnimatedVisibility(visible = showFullUserInfo) {
                UserInfoSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showFullUserInfo = !showFullUserInfo
                        },
                    user = user
                )
            }
        }
    }
}

@Composable
fun DrawerBody(
    items: List<MainContentMenuItem>,
    modifier: Modifier = Modifier,
    itemTextStyle: TextStyle = TextStyle(fontSize = 18.sp),
    onItemClick: (MainContentMenuItem) -> Unit
) {
    LazyColumn(modifier) {
        items(items) { item ->
            NavigationDrawerItem(
                label = {
                    Text(text = item.title, style = itemTextStyle)
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.contentDescription
                    )
                },
                selected = false,
                onClick = {
                    onItemClick(item)
                },
            )
        }
    }
}

@Composable
fun UserHead(
    id: String,
    firstName: String,
    lastName: String,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    textStyle: TextStyle = MaterialTheme.typography.labelMedium,
) {
    Box(modifier.size(size), contentAlignment = Alignment.Center) {
        val color = remember(id, firstName, lastName) {
            val name = listOf(firstName, lastName)
                .joinToString(separator = "")
                .uppercase()
            Color("$id / $name".toHslColor())
        }
        val initials = (firstName.take(1) + lastName.take(1)).uppercase()
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(SolidColor(color))
        }
        Text(text = initials, style = textStyle, color = Color.White)
    }
}

@Composable
fun UserInfoSection(
    modifier: Modifier = Modifier,
    user: NotesCollection
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.wrapContentHeight()
        ) {
            UserInfoTextWithTitle(title = R.string.settings_userInfo_email, subtitle = user.id)
            UserInfoTextWithTitle(title = R.string.settings_userInfo_website, subtitle = user.serverAddress)
            UserInfoTextWithTitle(title = R.string.settings_userInfo_city, subtitle = user.dateCreated)
            UserInfoTextWithTitle(title = R.string.settings_userInfo_state, subtitle = user.dateModified)
        }
    }
}

@Composable
private fun UserInfoTextWithTitle(@StringRes title: Int, subtitle: String?) {
    subtitle?.let {
        if (it.isNotBlank()) {
            TextWithOverline(title = title, subtitle = it)
        }
    }
}

@Composable @Preview
fun PreviewDrawer() {
    MainDrawer(
        //modifier = Modifier.weight(1f),
        user = mockNotesCollection(),
        versionInfo = "0.666-beta (666)",
        hideDonationButtons = false,
        donateButton = {
            DonateButtonPreview()
        },
        onItemClick = { }
    )
}

sealed class MainContentMenuItem(
    val id: String, // identifier, because title is subject to translations
    val title: String,
    val contentDescription: String,
    val icon: ImageVector
) {
    companion object {
        /**
         * TODO use reflection instead
         * workaround because we cannot save MainContentMenuItem into rememberSaveable
         */
        fun toMainContentMenuItem(id: String) =
            when (id) {
                "home" -> Home
                "settings" -> Settings
                "library" -> Library
                "logout" -> Logout
                "about" -> About
                "offline" -> Offline
                else -> throw IllegalArgumentException("$id is not a valid id")
            }

    }

    data object Home: MainContentMenuItem(id = "home", title = "Home", icon = Icons.Outlined.Home, contentDescription = "home")
    data object Settings: MainContentMenuItem(id = "settings", title = "Settings", icon = Icons.Outlined.Settings, contentDescription = "Settings")
    data object Library: MainContentMenuItem(id = "library", title = "Library", icon = Icons.Outlined.AddCircle, contentDescription = "Library")
    data object Offline: MainContentMenuItem(id = "offline", title = "Offline Songs", icon = Icons.Outlined.Call, contentDescription = "Offline Songs")
    data object About: MainContentMenuItem(id = "about", title = "About", icon = Icons.Outlined.Info, contentDescription = "About")
    data object Logout: MainContentMenuItem(id = "logout", title = "Logout", icon = Icons.Outlined.Send, contentDescription = "Logout")
}



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
                text = stringResource(id = title),
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
        title = R.string.app_name,
        subtitle = "R.string.settings_enableDebugLogging_title"
    )
}
