/**
 * Copyright (C) 2024  Antonio Tari
 *
 * This file is a part of Libre Notes
 * Android self-hosting, note-taking, client + server application
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
package luci.sixsixsix.homemessageshare.presentation.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
@Composable
fun NewNotesCollectionDialog(
    currentServer: String,
    currentCollectionName: String,
    onConfirm: (serverAddress: String, collectionName: String) -> Unit,
    onCancel: () -> Unit
) {
    var serverAddress by remember { mutableStateOf(currentServer) }
    var collectionName by remember { mutableStateOf(currentCollectionName) }

    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Card(
            modifier = Modifier
                .padding(16.dp),
            shape = RoundedCornerShape(8.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 30.dp, bottom = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                OutlinedTextField(
                    value = collectionName,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    onValueChange = {
                        collectionName = it
                    },
                    singleLine = true,
                    label = {
                        Text(
                            text = "Collection Name",
                            modifier = Modifier
                                //    .wrapContentSize(Alignment.Center)
                                .padding(vertical = 0.dp),
                            //textAlign = TextAlign.Center,
                            //fontWeight = FontWeight.Bold,
                            //fontSize = 15.sp
                        )
                    }
                )

                OutlinedTextField(
                    value = serverAddress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    onValueChange = {
                        serverAddress = it
                    },
                    singleLine = true,
                    label = {
                        Text(
                            text = "Server address",
                            modifier = Modifier
                                //    .wrapContentSize(Alignment.Center)
                                .padding(vertical = 0.dp),
                            //textAlign = TextAlign.Center,
                            //fontWeight = FontWeight.Bold,
                            //fontSize = 15.sp
                        )
                    }
                )

                Spacer(modifier = Modifier.padding(top = 20.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    RoundedCornerButton(
                        text = stringResource(id = android.R.string.cancel),
                        contentColor = MaterialTheme.colorScheme.primary,
                        borderEnabled = false,
                        onClick = onCancel
                    )

                    RoundedCornerButton(
                        text = stringResource(id = android.R.string.ok),
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        borderEnabled = false,
                        onClick = {
                            onConfirm(serverAddress, collectionName) }
                    )
                }
            }
        }
    }
}

@Composable
fun RoundedCornerButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    borderEnabled: Boolean = true,
    borderColor: Color = MaterialTheme.colorScheme.onSurface
) {
    TextButton(
        modifier = modifier
            .wrapContentSize(Alignment.Center),
        shape = RoundedCornerShape(25.dp),
        border = if (borderEnabled) BorderStroke(
            width = 0.5.dp,
            color = borderColor
        ) else null,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    ) {
        Box(
            modifier = Modifier
                .wrapContentSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                modifier = Modifier
                    .wrapContentSize(Alignment.Center)
                    .padding(
                        horizontal = 16.dp,
                        vertical = 4.dp
                    ),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp
            )
        }

    }
}


@Composable @Preview
fun PreviewNewPlaylistDialog() {
    NewNotesCollectionDialog(
        currentServer = "currentServer",
        currentCollectionName = "Collection Name",
        onCancel = { },
        onConfirm = { _, _ -> }
    )
}
