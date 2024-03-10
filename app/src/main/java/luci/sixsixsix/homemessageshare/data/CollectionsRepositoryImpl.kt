package luci.sixsixsix.homemessageshare.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import luci.sixsixsix.homemessageshare.common.Resource
import luci.sixsixsix.homemessageshare.data.local.NotesDatabase
import luci.sixsixsix.homemessageshare.data.local.entities.collectionEntityId
import luci.sixsixsix.homemessageshare.data.local.entities.toCollectionEntity
import luci.sixsixsix.homemessageshare.data.local.entities.toNotesCollection
import luci.sixsixsix.homemessageshare.data.remote.MainNetwork
import luci.sixsixsix.homemessageshare.domain.CollectionsRepository
import luci.sixsixsix.homemessageshare.domain.models.NotesCollection
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

    override suspend fun getCollection(
        serverName: String,
        collectionName: String
    ) = flow {
        emit(Resource.Loading(true))
        val notesDb = dao.getCollection(
            collectionEntityId(serverName, collectionName)
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

    override suspend fun deleteCollection(
        serverName: String,
        collectionName: String
    ) = flow {
        emit(Resource.Loading(true))
        dao.deleteCollection(collectionEntityId(collectionName, serverName))
        emit(Resource.Success(getAllCollections()))
        emit(Resource.Loading(false))
    }.catch { e -> emit(Resource.Error(exception = e)) }
}
