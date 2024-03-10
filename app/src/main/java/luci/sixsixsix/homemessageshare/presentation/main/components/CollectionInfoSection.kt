package luci.sixsixsix.homemessageshare.presentation.main.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import luci.sixsixsix.homemessageshare.R
import luci.sixsixsix.homemessageshare.domain.models.NotesCollection

@Composable
fun CollectionInfoSection(
    modifier: Modifier = Modifier,
    notesCollection: NotesCollection
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
            CollectionInfoTextWithTitle(title = R.string.settings_userInfo_email, subtitle = notesCollection.collectionName)
            CollectionInfoTextWithTitle(title = R.string.settings_userInfo_website, subtitle = notesCollection.serverAddress)
            CollectionInfoTextWithTitle(title = R.string.settings_userInfo_city, subtitle = notesCollection.dateCreated)
            CollectionInfoTextWithTitle(title = R.string.settings_userInfo_state, subtitle = notesCollection.dateModified)
        }
    }
}
