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
package luci.sixsixsix.homemessageshare.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import luci.sixsixsix.homemessageshare.common.Constants
import luci.sixsixsix.homemessageshare.common.Constants.COLLECTION_LOCALHOST_ADDRESS
import luci.sixsixsix.homemessageshare.common.Constants.COLLECTION_LOCALHOST_NAME
import luci.sixsixsix.homemessageshare.common.Constants.COLOUR_NOTE_DEFAULT
import luci.sixsixsix.homemessageshare.common.Constants.COLOUR_NOTE_OFFLINE
import luci.sixsixsix.homemessageshare.common.Constants.THEME_NOTE_DEFAULT
import luci.sixsixsix.homemessageshare.common.getDateString
import luci.sixsixsix.homemessageshare.domain.models.NotesCollection.Companion.localhostCollectionId

@Parcelize
data class NotesCollection(
    val collectionName: String,
    val dateCreated: String,
    val dateModified: String,
    val colour: String,
    val tags: List<String> = listOf(),
    val serverAddress: String,
    val appTheme: String
): Parcelable {
    val id by lazy { collectionId(serverAddress, collectionName) }

    companion object {
        val localhostCollectionId = collectionId(COLLECTION_LOCALHOST_ADDRESS, COLLECTION_LOCALHOST_NAME)

        fun defaultLocalhost() = NotesCollection(
            collectionName = COLLECTION_LOCALHOST_NAME,
            dateCreated = getDateString(),
            dateModified = getDateString(),
            colour = COLOUR_NOTE_OFFLINE,
            tags = listOf(),
            serverAddress = COLLECTION_LOCALHOST_ADDRESS,
            appTheme = Constants.THEME_DARK_YOU
        )

        fun newNotesCollection(serverAddress: String, collectionName: String) = NotesCollection(
            collectionName = collectionName,
            serverAddress = serverAddress,
            dateCreated = getDateString(),
            dateModified = getDateString(),
            tags = listOf(),
            appTheme = THEME_NOTE_DEFAULT,
            colour = COLOUR_NOTE_DEFAULT
        )
    }
}

fun NotesCollection.isLocalhost() =
    id == localhostCollectionId

fun NotesCollection.isMaterialYou() =
    appTheme == Constants.THEME_DARK_YOU

fun collectionId(serverAddress: String, collectionName: String) =
    "$serverAddress$collectionName"