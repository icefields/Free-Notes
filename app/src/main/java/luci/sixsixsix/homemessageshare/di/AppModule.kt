package luci.sixsixsix.homemessageshare.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import luci.sixsixsix.homemessageshare.data.remote.MainNetwork
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

typealias WeakContext = WeakReference<Context>

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .connectTimeout(4000, TimeUnit.SECONDS)
            .readTimeout(4000, TimeUnit.SECONDS)
            .writeTimeout(8000, TimeUnit.SECONDS)
            //.addInterceptor(interceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl("http://192.168.1.100/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): MainNetwork =
        retrofit.create(MainNetwork::class.java)

    @Provides
    @Singleton
    fun provideWeakApplicationContext(application: Application) =
        WeakContext(application.applicationContext)
}
