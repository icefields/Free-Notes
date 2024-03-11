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
package luci.sixsixsix.homemessageshare.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import com.google.gson.Gson
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import luci.sixsixsix.homemessageshare.common.Resource
import luci.sixsixsix.homemessageshare.data.local.NotesDatabase
import luci.sixsixsix.homemessageshare.data.local.entities.toCollectionEntity
import luci.sixsixsix.homemessageshare.data.local.entities.toNoteEntity
import luci.sixsixsix.homemessageshare.data.local.entities.toNotesCollection
import luci.sixsixsix.homemessageshare.data.remote.MainNetwork
import luci.sixsixsix.homemessageshare.data.remote.dto.MessagesDto
import luci.sixsixsix.homemessageshare.data.remote.dto.toMessage
import luci.sixsixsix.homemessageshare.domain.CollectionsRepository
import luci.sixsixsix.homemessageshare.domain.models.NotesCollection
import luci.sixsixsix.homemessageshare.domain.models.collectionId
import javax.inject.Inject

class CollectionsRepositoryImpl @Inject constructor(
    private val api: MainNetwork,
    db: NotesDatabase
): CollectionsRepository {
    private val dao = db.dao

    override val collectionsLiveData: LiveData<List<NotesCollection>> =
        dao.getCollectionsLiveData().distinctUntilChanged().map { entities ->
            entities.map { it.toNotesCollection() }
        }

    override suspend fun initializeOfflineUser() = flow {
        emit(Resource.Loading(true))
        println("aaaa CollectionsRepositoryImpl initializeOfflineUser start")

        val offlineCollection = NotesCollection.defaultLocalhost()
        dao.insertCollection(offlineCollection.toCollectionEntity())
        Gson().fromJson(offlineUserData, MessagesDto::class.java).data.map { it.toMessage() }.run {
            dao.insertNotes(map { it.toNoteEntity(offlineCollection.collectionName) })
        }
        emit(Resource.Success(getAllCollections()))
        emit(Resource.Loading(false))
    }.catch { e -> emit(Resource.Error(exception = e)) }

    override suspend fun getCollection(
        serverName: String,
        collectionName: String
    ) = flow {
        emit(Resource.Loading(true))
        if (collectionName.isNullOrBlank())
            throw NullPointerException("COLLECTION NAME IS NULL OR EMPTY")
        val notesDb = dao.getCollection(
            collectionId(serverName, collectionName)
        ).toNotesCollection()
        emit(Resource.Success(notesDb))
        emit(Resource.Loading(false))
    }.catch { e -> emit(Resource.Error(exception = e)) }

    private suspend fun getAllCollections() =
        dao.getCollections().map { it.toNotesCollection() }

    override suspend fun writeCollection(notesCollection: NotesCollection) = flow {
        emit(Resource.Loading(true))
        dao.insertCollection(notesCollection.toCollectionEntity())
        emit(Resource.Success(getAllCollections()))
        emit(Resource.Loading(false))
    }.catch { e -> emit(Resource.Error(exception = e)) }

    override suspend fun deleteCollection(notesCollection: NotesCollection) = flow {
        emit(Resource.Loading(true))
        dao.deleteCollection(notesCollection.id)
        emit(Resource.Success(getAllCollections()))
        emit(Resource.Loading(false))
    }.catch { e -> emit(Resource.Error(exception = e)) }
}

const val offlineUserData = "{\n" +
        "    \"data\": [\n" +
        "        {\n" +
        "            \"id\": \"65e5330d8b6a2\",\n" +
        "            \"title\": \"first\",\n" +
        "            \"message\": \"first real note\",\n" +
        "            \"date_created\": \"2024-03-04  02:33:49\",\n" +
        "            \"date_modified\": \"2024-03-04  02:33:49\",\n" +
        "            \"tags\": [\"freenote\", \"note\"]\n" +
        "        },\n" +
        "        {\n" +
        "            \"id\": \"65e6b108a7ddd\",\n" +
        "            \"title\": \"How to Remove Notes\",\n" +
        "            \"message\": \"remove notes by swiping left to right\",\n" +
        "            \"date_created\": \"2024-03-04  02:33:49\",\n" +
        "            \"date_modified\": \"2024-03-04  02:33:49\",\n" +
        "            \"tags\": [\"freenote\", \"note\"]\n" +
        "        },\n" +
        "        {\n" +
        "            \"id\": \"65e6b2797132e\",\n" +
        "            \"title\": \"How to Edit Notes\",\n" +
        "            \"message\": \"edit notes by swiping right to left\",\n" +
        "            \"date_created\": \"2024-03-04  02:33:49\",\n" +
        "            \"date_modified\": \"2024-03-04  02:33:49\",\n" +
        "            \"tags\": [\"freenote\", \"note\"]\n" +
        "        }\n" +
        "    ]\n" +
        "}"