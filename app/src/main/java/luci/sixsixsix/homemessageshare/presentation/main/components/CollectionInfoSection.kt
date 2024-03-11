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
