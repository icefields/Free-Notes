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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import luci.sixsixsix.homemessageshare.common.mockNotesCollection
import luci.sixsixsix.homemessageshare.common.toHslColor
import luci.sixsixsix.homemessageshare.domain.models.NotesCollection
import luci.sixsixsix.homemessageshare.presentation.common.DonateButton
import luci.sixsixsix.homemessageshare.presentation.common.DonateButtonPreview

@Composable
fun MainDrawer(
    currentNotesCollection: NotesCollection,
    versionInfo: String,
    hideDonationButtons: Boolean,
    items: List<NotesCollection>,
    onItemClick: (NotesCollection) -> Unit,
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
        DrawerHeader(currentNotesCollection)
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
fun DrawerHeader(notesCollection: NotesCollection) {
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
                UserHead(
                    id = notesCollection.collectionName,
                    firstName = notesCollection.serverAddress,
                    lastName = notesCollection.dateCreated
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 20.dp)
                ) {
                    Text(
                        text = notesCollection.collectionName,
                        fontSize = 18.sp,
                        maxLines = 1,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight.Normal
                    )
                    Text(
                        text = notesCollection.serverAddress,
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
                CollectionInfoSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showFullUserInfo = !showFullUserInfo
                        },
                    notesCollection = notesCollection
                )
            }
        }
    }
}

@Composable
fun DrawerBody(
    items: List<NotesCollection>,
    modifier: Modifier = Modifier,
    itemTextStyle: TextStyle = TextStyle(fontSize = 18.sp),
    onItemClick: (NotesCollection) -> Unit
) {
    println("DRAWER aaaa items ${items.size}")
    LazyColumn(modifier) {
        items(items) { item ->
            NavigationDrawerItem(
                label = {
                    Text(text = item.collectionName, style = itemTextStyle)
                },
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.ArrowForward,
                        contentDescription = "Collection ${item.collectionName}"
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

@Composable @Preview
fun PreviewDrawer() {
    MainDrawer(
        //modifier = Modifier.weight(1f),
        currentNotesCollection = mockNotesCollection(),
        versionInfo = "0.666-beta (666)",
        hideDonationButtons = false,
        onItemClick = {},
        items = listOf(mockNotesCollection(), mockNotesCollection(), mockNotesCollection(), mockNotesCollection(), mockNotesCollection()),
        donateButton = {
            DonateButtonPreview()
        })
}
