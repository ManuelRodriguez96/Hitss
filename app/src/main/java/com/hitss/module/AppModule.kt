package com.hitss.module

import android.content.Context
import com.hitss.data.DataRepository
import com.hitss.data.remote.RemoteData
import com.hitss.data.remote.service.ServiceGenerator
import com.hitss.utils.Network
import com.hitss.utils.NetworkConnectivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideCoroutineContext(): CoroutineContext {
        return Dispatchers.IO
    }

    @Provides
    @Singleton
    fun provideNetworkConnectivity(@ApplicationContext context: Context): NetworkConnectivity {
        return Network(context)
    }

    @Provides
    @Singleton
    fun provideServiceGenerator() : ServiceGenerator {
        return ServiceGenerator()
    }

    @Provides
    @Singleton
    fun provideRemoteRepository(@ApplicationContext context : Context) : RemoteData {
        return RemoteData(provideServiceGenerator(),provideNetworkConnectivity(context))
    }

    @Provides
    @Singleton
    fun provideDataRepository(@ApplicationContext context : Context) : DataRepository {
        return DataRepository(provideRemoteRepository(context),provideCoroutineContext())
    }
}
