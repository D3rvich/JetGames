package ru.d3rvich.data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.koin.core.annotation.Single
import ru.d3rvich.datastore.JetGamesPreferencesDataStore
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun providePreferencesDatastore(@ApplicationContext context: Context): JetGamesPreferencesDataStore {
        return JetGamesPreferencesDataStore(context = context)
    }
}

@org.koin.core.annotation.Module
object KoinDataStoreModule {

    @Single
    fun dataStore(context: Context) = JetGamesPreferencesDataStore(context = context)
}