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
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import luci.sixsixsix.homemessageshare.common.mockNotesCollection
import luci.sixsixsix.homemessageshare.domain.models.Message
import luci.sixsixsix.homemessageshare.presentation.SettingsViewModel
import luci.sixsixsix.homemessageshare.presentation.common.NewServerDialog
import luci.sixsixsix.homemessageshare.presentation.main.components.MainDrawer
import luci.sixsixsix.homemessageshare.presentation.main.components.MessageItem

@Composable
fun MainScreen(
    settingsViewModel: SettingsViewModel,
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val state = settingsViewModel.state
    MainScreenContent(
        messages = mainViewModel.state.messages,
        currentServer = state.serverAddress,
        currentUsername = state.username,
        isMaterialYouOn = state.isMaterialYouEnabled,
        onSubmitMessage = mainViewModel::submitMessage,
        onSyncNotes = mainViewModel::syncNotes,
        onRemoveMessage = mainViewModel::removeMessage,
        onEditMessage = mainViewModel::editMessage,
        onNewServer = settingsViewModel::setServer,
        onNewUsername = settingsViewModel::setUsername,
        onToggleMaterialYou = settingsViewModel::toggleMaterialYou
    ) { }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MainScreenContent(
    messages: List<Message>,
    currentServer: String,
    currentUsername: String,
    isMaterialYouOn: Boolean,
    onSubmitMessage: (message: String, title: String, tags: List<String>) -> Unit,
    onSyncNotes: () -> Unit,
    onRemoveMessage: (message: Message) -> Unit,
    onEditMessage: (message: Message) -> Unit,
    onNewServer: (server: String) -> Unit,
    onToggleMaterialYou: (enable: Boolean) -> Unit,
    onNewUsername: (username: String) -> Unit,
    onNavigationIconClick: (username: String) -> Unit
) {
    var title by rememberSaveable { mutableStateOf("") }
    var message by rememberSaveable { mutableStateOf("") }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val scope = rememberCoroutineScope()
    var createPlaylistDialogOpen by remember { mutableStateOf(false) }
    var editUsernameOpen by remember { mutableStateOf(false) }
    var editModeEnabled by remember { mutableStateOf(false) }
    val drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed)

    if (createPlaylistDialogOpen) {
        NewServerDialog(
            currentServer = currentServer,
            onConfirm = {
                onNewServer(it)
                createPlaylistDialogOpen = false
            },
            onCancel = {
                createPlaylistDialogOpen = false
            }
        )
    }

    if (editUsernameOpen) {
        NewServerDialog(
            currentServer = currentUsername,
            onConfirm = {
                onNewUsername(it)
                editUsernameOpen = false
            },
            onCancel = {
                editUsernameOpen = false
            }
        )
    }
    ModalNavigationDrawer(
        drawerState = drawerState,
        //scrimColor = MaterialTheme.colorScheme.scrim,
        drawerContent = {
            MainDrawer(
                user = mockNotesCollection(),
                versionInfo = "settingsViewModel.state.appVersionInfoStr",
                hideDonationButtons = false,
                onItemClick = {
                    scope.launch {
                        drawerState.close()
                    }
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
                    /*modifier = Modifier.background(Color.Transparent),
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface,
                ),*/
                    title = {
                        Text(
                            modifier = Modifier
                                .basicMarquee()
                                .padding(15.dp)
                                .clickable {
                                    onToggleMaterialYou(!isMaterialYouOn)
                                },
                            text = "NOTES",
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
                            editUsernameOpen = true
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.AccountCircle,
                                contentDescription = "show hide album info"
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
                            IconButton(onClick = {
                                onNavigationIconClick(currentUsername)
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
                                onRemove = onRemoveMessage,
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
