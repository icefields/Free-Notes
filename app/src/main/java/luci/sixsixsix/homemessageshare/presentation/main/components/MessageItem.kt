package luci.sixsixsix.homemessageshare.presentation.main.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import luci.sixsixsix.homemessageshare.domain.models.Message
import luci.sixsixsix.homemessageshare.presentation.common.SwipeToDismissItem

@Composable
fun MessageItem(
    message: Message,
    modifier: Modifier = Modifier,
    enableSwipeToRemove: Boolean,
    onRemove: (Message) -> Unit
) {
    SwipeToDismissItem(
        item = message,
        foregroundView = {
            Card(
                shape = RoundedCornerShape(8.dp),
            ) {
                Column {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        text = message.date,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Light
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        text = message.title ?: "",
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = message.message ?: "",
                        onValueChange = {},
                        readOnly = true,
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            cursorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        )
                    )
                }
            }
        },
        enableSwipeToRemove = enableSwipeToRemove,
        onRemove = onRemove,
        onRightToLeftSwipe = onRemove,
        iconRight = Icons.Default.Delete,
    )
}