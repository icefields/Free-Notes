package luci.sixsixsix.homemessageshare.presentation.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import luci.sixsixsix.homemessageshare.domain.models.Message
import luci.sixsixsix.homemessageshare.presentation.common.SwipeToDismissItem

@Composable
fun MessageItem(
    message: Message,
    modifier: Modifier = Modifier,
    enableSwipeToRemove: Boolean,
    onRemove: (Message) -> Unit,
    onEdit: (Message) -> Unit,
) {
    var enlarged by rememberSaveable{ mutableStateOf(false) }

    SwipeToDismissItem(
        item = message,
        foregroundView = {
            Card(
                modifier = modifier.clickable {
                    enlarged = !enlarged
                },
                shape = RoundedCornerShape(8.dp),
            ) {
                SelectionContainer{
                    Column(modifier = Modifier.fillMaxWidth()) {
                        if (!message.title.isNullOrBlank()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 8.dp)
                                        .padding(top = 10.dp, bottom = 8.dp),
                                    text = message.title,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                                    maxLines = 3,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                        if (!message.message.isNullOrBlank()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .background(MaterialTheme.colorScheme.primaryContainer)
                            ) {
                                Text(
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp, vertical = 10.dp)
                                        .heightIn(max = 150.dp)
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.primaryContainer)
                                        .verticalScroll(rememberScrollState()),
                                    text = message.message,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        // footer DATE row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                        ) {
                            Text(
                                text = message.dateCreated,
                                maxLines = 1,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Light
                            )
                            Text(
                                modifier = Modifier
                                    .padding(start = 8.dp),
                                text = message.dateModified,
                                maxLines = 1,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Light
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            modifier = Modifier
                                .padding(start = 8.dp),
                            text = message.id,
                            maxLines = 1,
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.Thin
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        },
        enableSwipeToRemove = enableSwipeToRemove,
        onRemove = onRemove,
        onRightToLeftSwipe = onEdit,
        iconRight = Icons.Default.Delete,
    )
}