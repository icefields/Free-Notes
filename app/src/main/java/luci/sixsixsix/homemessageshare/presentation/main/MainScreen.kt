package luci.sixsixsix.homemessageshare.presentation.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import luci.sixsixsix.homemessageshare.domain.models.Message
import luci.sixsixsix.homemessageshare.presentation.common.NewServerDialog
import luci.sixsixsix.homemessageshare.presentation.main.components.MessageItem

@Composable
fun MainScreen(
    mainViewModel: MainViewModel = hiltViewModel()
) {
    MainScreenContent(
        messages = mainViewModel.state.messages,
        currentServer = mainViewModel.state.serverAddress,
        onSubmitMessage = mainViewModel::submitMessages,
        onRemoveMessage = mainViewModel::removeMessages,
        onNewServer = mainViewModel::setServer
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MainScreenContent(
    messages: List<Message>,
    currentServer: String,
    onSubmitMessage: (title: String, message: String) -> Unit,
    onRemoveMessage: (message: Message) -> Unit,
    onNewServer: (server: String) -> Unit
) {
    var title by remember {
        mutableStateOf("")
    }
    var message by remember {
        mutableStateOf("")
    }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    var createPlaylistDialogOpen by remember { mutableStateOf(false) }

    if (createPlaylistDialogOpen) {
        NewServerDialog(
            currentServer = currentServer,
            onConfirm = onNewServer,
            onCancel = {
                createPlaylistDialogOpen = false
            }
        )
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = Color.Transparent,
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
                            .padding(15.dp),
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
                    IconButton(onClick = {
                        createPlaylistDialogOpen = true
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = "show hide album info"
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
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = title,
                    onValueChange = {
                        title = it
                    },
                    label = {
                        Text(text = "Title")
                    }
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = message,
                    onValueChange = {
                        message = it
                    },
                    label = {
                        Text(text = "Note")
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        onSubmitMessage(title, message)
                        title = ""
                        message = ""
                    }
                ) {
                    Text(text = "SUBMIT")
                }
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(messages) { mess ->
                        MessageItem(
                            message = mess,
                            enableSwipeToRemove = true,
                            onRemove = onRemoveMessage
                        )
                        Spacer(modifier = Modifier.height(8.dp))
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
