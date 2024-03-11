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

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.distinctUntilChanged
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import luci.sixsixsix.homemessageshare.data.local.NotesDatabase
import luci.sixsixsix.homemessageshare.data.local.entities.toNotesCollection
import luci.sixsixsix.homemessageshare.di.WeakContext
import luci.sixsixsix.homemessageshare.domain.SettingsRepository
import luci.sixsixsix.homemessageshare.domain.Success
import luci.sixsixsix.homemessageshare.domain.models.NotesCollection
import luci.sixsixsix.homemessageshare.domain.models.collectionId
import javax.inject.Inject
import javax.inject.Singleton

private const val KEY_STORAGE = "{prefix}KEY_WORKER_PREFERENCE"
private const val KEY_COLLECTION_ID_PREFERENCE_ID = "{prefix}downloadWorkerId"
private const val KEY_USERNAME_PREFERENCE_ID = "{prefix}username.id.storage"
private const val KEY_MATERIAL_YOU_PREFERENCE_ID = "{prefix}materiayouonoff.id.storage"

@OptIn(DelicateCoroutinesApi::class)
@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val weakContext: WeakContext,
    db: NotesDatabase
): SettingsRepository {
    private val dao = db.dao

    private val _settingsLiveData: MutableLiveData<NotesCollection?> = MutableLiveData(null)
    override val settingsLiveData: LiveData<NotesCollection?> = _settingsLiveData

    override val settingsFlow = settingsLiveData.distinctUntilChanged().asFlow().filterNotNull()

    private fun getSharedPreferences() =
        weakContext.get()?.getSharedPreferences(KEY_STORAGE, Context.MODE_PRIVATE)

    init {
        initialize { _settingsLiveData.value = it }
    }

    private fun initialize(callback: (NotesCollection) -> Unit) {
        GlobalScope.launch {
            dao.getCollection(getCurrentNotesCollectionId(true))
            withContext(Dispatchers.Main) {
                try {
                    callback(getCurrentNotesCollection(true))
                }catch (e: Exception) {
                    println("initialize settings repo aaaa ${e.stackTraceToString()}")
                }
            }
        }
    }

    @SuppressLint("ApplySharedPref")
    private suspend fun writeString(
        key: String,
        serverAddress: String
    ): Success =
        getSharedPreferences()?.let { sharedPreferences ->
            withContext(Dispatchers.IO) {
                val editor = sharedPreferences.edit()
                editor.apply {
                    putString(key, serverAddress)
                    commit()
                }
                return@withContext true
            }
        } ?: false


    override suspend fun getCurrentNotesCollection(returnDefaultIfNull: Boolean) =
        dao.getCollection(getCurrentNotesCollectionId(returnDefaultIfNull)).toNotesCollection()

    override suspend fun writeCurrentNotesCollectionId(
        serverAddress: String,
        collectionName: String
    ) = writeString(KEY_COLLECTION_ID_PREFERENCE_ID, collectionId(serverAddress, collectionName)).also {
            _settingsLiveData.value = getCurrentNotesCollection(true)
        }

    private fun getCurrentNotesCollectionId(returnDefaultIfNull: Boolean): String =
        getSharedPreferences()?.getString(KEY_COLLECTION_ID_PREFERENCE_ID, null) ?: run {
        if (returnDefaultIfNull) {
            NotesCollection.localhostCollectionId
        } else ""
    }
}
