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
package luci.sixsixsix.homemessageshare.presentation.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import luci.sixsixsix.homemessageshare.R
import luci.sixsixsix.homemessageshare.common.mockNotesCollection
import luci.sixsixsix.homemessageshare.domain.models.Message
import luci.sixsixsix.homemessageshare.domain.models.NotesCollection
import luci.sixsixsix.homemessageshare.domain.models.isMaterialYou
import luci.sixsixsix.homemessageshare.presentation.common.NewNotesCollectionDialog
import luci.sixsixsix.homemessageshare.presentation.dialog.EraseConfirmDialog
import luci.sixsixsix.homemessageshare.presentation.main.components.MainDrawer
import luci.sixsixsix.homemessageshare.presentation.main.components.MessageItem

@Composable
fun MainScreen(
    mainViewModel: MainViewModel
) {
    val state = mainViewModel.state
    MainScreenContent(
        messages = state.messages,
        items = state.collections,
        currentCollection = state.notesCollection,
        onSubmitMessage = mainViewModel::submitMessage,
        onSyncNotes = mainViewModel::syncNotes,
        onRemoveMessage = mainViewModel::removeMessage,
        onEditMessage = mainViewModel::editMessage,
        onNewCollection = mainViewModel::onNewConfiguration,
        onToggleMaterialYou = mainViewModel::toggleMaterialYou,
        onDeleteCollection = mainViewModel::deleteCollection,
        onCollectionSelected = mainViewModel::switchDisplayedCollection
    ) { }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MainScreenContent(
    messages: List<Message>,
    currentCollection: NotesCollection,
    items: List<NotesCollection>,
    onSubmitMessage: (message: String, title: String, tags: List<String>) -> Unit,
    onSyncNotes: () -> Unit,
    onRemoveMessage: (message: Message) -> Unit,
    onEditMessage: (message: Message) -> Unit,
    onToggleMaterialYou: (enable: Boolean) -> Unit,
    onNewCollection:  (serverAddress: String, collectionName: String) -> Unit,
    onCollectionSelected: (notesCollection: NotesCollection) -> Unit,
    onDeleteCollection: (NotesCollection) -> Unit,
    onNavigationIconClick: (username: String) -> Unit // doing nothing for now
) {
    var title by rememberSaveable { mutableStateOf("") }
    var message by rememberSaveable { mutableStateOf("") }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val scope = rememberCoroutineScope()
    var createPlaylistDialogOpen by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf<Message?>(null) }
    var editModeEnabled by remember { mutableStateOf(false) }
    val drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed)
    val isOfflineCollection = false
    val currentServer = currentCollection.serverAddress
    val currentCollectionName = currentCollection.collectionName
    val isMaterialYouOn = currentCollection.isMaterialYou()

    if (createPlaylistDialogOpen) {
        NewNotesCollectionDialog(
            currentServer = currentServer,
            currentCollectionName = currentCollectionName,
            onConfirm = { newServer, newCollectionName ->
                onNewCollection(newServer, newCollectionName)
                createPlaylistDialogOpen = false
            },
            onCancel = {
                createPlaylistDialogOpen = false
            }
        )
    }

    if (showDeleteDialog != null) {
        showDeleteDialog?.let { mess: Message ->
            EraseConfirmDialog(
                onDismissRequest = {
                    showDeleteDialog = null
                },
                onConfirmation = {
                    onRemoveMessage(it)
                    showDeleteDialog = null
                },
                data = mess,
                dialogTitle = R.string.remove_title,
                dialogText = R.string.remove_subtitle
            )
        }

    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            MainDrawer(
                currentNotesCollection = currentCollection,
                items = items,
                versionInfo = "settingsViewModel.state.appVersionInfoStr",
                hideDonationButtons = false,
                onDeleteCollection = onDeleteCollection,
                onItemClick = {
                    scope.launch {
                        drawerState.close()
                    }
                    onCollectionSelected(it)
                }
            )
        }
    ) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            containerColor = Color.Transparent,
            floatingActionButton = {
                IconButton(
                    modifier = Modifier.size(96.dp),
                    onClick = { editModeEnabled = !editModeEnabled }) {
                    Icon(
                        modifier = Modifier.size(96.dp),
                        imageVector = Icons.Outlined.AddCircle,
                        contentDescription = "add new note"
                    )
                }
            },
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            modifier = Modifier
                                .basicMarquee()
                                .padding(15.dp)
                                .clickable {
                                    onToggleMaterialYou(!isMaterialYouOn)
                                },
                            text = currentCollection.collectionName,
                            maxLines = 1,
                            fontWeight = FontWeight.Normal,
                            style = TextStyle(
                                fontSize = 16.sp,
                                shadow = Shadow(
                                    color = MaterialTheme.colorScheme.background,
                                    offset = Offset(0.0f, 0.0f),
                                    blurRadius = 32f
                                )
                            ),
                            fontSize = 16.sp
                        )
                    },
                    scrollBehavior = scrollBehavior,
                    actions = {
                        IconButton(onClick = onSyncNotes) {
                            Icon(
                                imageVector = Icons.Outlined.Refresh,
                                contentDescription = "sync all the offline notes"
                            )
                        }
                        IconButton(onClick = {
                            createPlaylistDialogOpen = true
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.Settings,
                                contentDescription = "show hide album info"
                            )
                        }
                    },
                    navigationIcon = {
                        if (!isOfflineCollection) {
                            IconButton(onClick = {
                                onNavigationIconClick(currentCollectionName)
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Menu"
                                )

                            }
                        }
                    }
                )
            }
        ) {
            Surface(
                modifier = Modifier
                    .padding(it)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                color = Color.Transparent
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    AnimatedVisibility(visible = editModeEnabled) {
                        Column() {
                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = title,
                                onValueChange = { newValue ->
                                    title = newValue
                                },
                                label = {
                                    Text(text = "Note")
                                }
                            )
                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = message,
                                onValueChange = { newValue ->
                                    message = newValue
                                },
                                label = {
                                    Text(text = "Details")
                                }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    onSubmitMessage(message, title, listOf())
                                    title = ""
                                    message = ""
                                }
                            ) {
                                Text(text = "SUBMIT")
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(messages) { mess ->
                            MessageItem(
                                message = mess,
                                enableSwipeToRemove = true,
                                onRemove = { mess ->
                                    showDeleteDialog = mess
                                },
                                onEdit = { mess ->
                                    onEditMessage(mess.copy(message = message, title = title))
                                }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
//    HomeMessageShareTheme {
//        MainScreenContent(listOf("message 1", "message 2")) { a,b ->
//
//        }
//    }
}
