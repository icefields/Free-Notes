package luci.sixsixsix.homemessageshare.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import luci.sixsixsix.homemessageshare.data.CollectionsRepositoryImpl
import luci.sixsixsix.homemessageshare.data.MessagesRepositoryImpl
import luci.sixsixsix.homemessageshare.data.SettingsRepositoryImpl
import luci.sixsixsix.homemessageshare.data.remote.FreeNotesInterceptor
import luci.sixsixsix.homemessageshare.domain.CollectionsRepository
import luci.sixsixsix.homemessageshare.domain.MessagesRepository
import luci.sixsixsix.homemessageshare.domain.SettingsRepository
import okhttp3.Interceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindMusicRepository(
        messagesRepositoryImpl: MessagesRepositoryImpl
    ): MessagesRepository

    @Binds
    @Singleton
    abstract fun bindInterceptor(
        interceptor: FreeNotesInterceptor
    ): Interceptor

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        settingsRepositoryImpl: SettingsRepositoryImpl
    ): SettingsRepository

    @Binds
    @Singleton
    abstract fun bindCollectionsRepository(
        collectionsRepositoryImpl: CollectionsRepositoryImpl
    ): CollectionsRepository
}
