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

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import luci.sixsixsix.homemessageshare.domain.models.Message
import luci.sixsixsix.homemessageshare.domain.models.NotesCollection

@Parcelize
data class MainState(
    //val username: String = Constants.COLLECTION_LOCALHOST_NAME,
    val notesCollection: NotesCollection = NotesCollection.defaultLocalhost(),
    val messages: List<Message> = listOf(),
    val loading: Boolean = false,
    val collections: List<NotesCollection> = listOf()
): Parcelable
