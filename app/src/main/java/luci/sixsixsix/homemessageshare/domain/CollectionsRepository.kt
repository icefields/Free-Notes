package luci.sixsixsix.homemessageshare.domain

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import luci.sixsixsix.homemessageshare.common.Resource
import luci.sixsixsix.homemessageshare.domain.models.NotesCollection
import luci.sixsixsix.homemessageshare.domain.models.Settings

interface CollectionsRepository {
    val collectionsLiveData: LiveData<List<NotesCollection>>
    suspend fun getCollection(serverName: String, collectionName: String): Flow<Resource<NotesCollection>>
    suspend fun writeCollection(notesCollection: NotesCollection): Flow<Resource<List<NotesCollection>>>
    suspend fun deleteCollection(serverName: String, collectionName: String): Flow<Resource<List<NotesCollection>>>
}
