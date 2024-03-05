package luci.sixsixsix.homemessageshare.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import luci.sixsixsix.homemessageshare.data.MessagesRepositoryImpl
import luci.sixsixsix.homemessageshare.domain.MessagesRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindMusicRepository(
        messagesRepositoryImpl: MessagesRepositoryImpl
    ): MessagesRepository
}
