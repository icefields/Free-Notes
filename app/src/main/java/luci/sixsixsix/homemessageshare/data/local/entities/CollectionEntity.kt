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
package luci.sixsixsix.homemessageshare.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import luci.sixsixsix.homemessageshare.domain.models.NotesCollection

@Entity
data class CollectionEntity(
    @PrimaryKey val id: String, // = "$serverAddress$collectionName"
    val dateCreated: String,
    val dateModified: String,
    val colour: String,
    //val tags: List<String>,
    val collectionName: String,
    val serverAddress: String,
    val appTheme: String
)

fun CollectionEntity.toNotesCollection() = NotesCollection(
    collectionName = collectionName,
    dateCreated = dateCreated,
    dateModified = dateModified,
    colour = colour,
    serverAddress = serverAddress,
    appTheme = appTheme
)

fun NotesCollection.toCollectionEntity() = CollectionEntity(
    collectionName = collectionName,
    id = id,
    dateCreated = dateCreated,
    dateModified = dateModified,
    colour = colour,
    serverAddress = serverAddress,
    appTheme = appTheme
)
