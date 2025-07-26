package ru.d3rvich.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ru.d3rvich.core.domain.repositories.GamesRepository
import ru.d3rvich.core.domain.repositories.GenresRepository
import ru.d3rvich.core.domain.repositories.PlatformsRepository
import ru.d3rvich.core.domain.repositories.SettingsRepository
import ru.d3rvich.data.model.SyncField
import ru.d3rvich.data.model.SyncTimeManagerImpl
import ru.d3rvich.data.paging.GamesPagingSource
import ru.d3rvich.data.repositoties.GamesRepositoryImpl
import ru.d3rvich.data.repositoties.GenresRepositoryImpl
import ru.d3rvich.data.repositoties.PlatformsRepositoryImpl
import ru.d3rvich.data.repositoties.SettingsRepositoryImpl
import ru.d3rvich.database.JetGamesDatabase
import ru.d3rvich.datastore.JetGamesPreferencesDataStore
import ru.d3rvich.remote.JetGamesApiService

/**
 * Created by Ilya Deryabin at 01.02.2024
 */
@Module
@InstallIn(ViewModelComponent::class)
internal object DataModule {

    @Provides
    fun provideGamesRepository(
        pagingSourceFactory: GamesPagingSource.GamesPagingSourceFactory,
        apiService: JetGamesApiService,
        database: JetGamesDatabase,
    ): GamesRepository = GamesRepositoryImpl(
        gamesPagingSourceFactory = pagingSourceFactory,
        apiService = apiService,
        database = database
    )

    @Provides
    fun providePlatformsRepository(
        apiService: JetGamesApiService,
        database: JetGamesDatabase,
        syncManagerFactory: SyncTimeManagerImpl.Factory,
    ): PlatformsRepository = PlatformsRepositoryImpl(
        apiService = apiService,
        database = database,
        syncTimeManager = syncManagerFactory.create(SyncField.Platforms)
    )

    @Provides
    fun providesGenresRepository(
        apiService: JetGamesApiService,
        database: JetGamesDatabase,
        syncManagerFactory: SyncTimeManagerImpl.Factory,
    ): GenresRepository = GenresRepositoryImpl(
        apiService = apiService,
        database = database,
        syncTimeManager = syncManagerFactory.create(SyncField.Genres)
    )

    @Provides
    fun provideSettingsRepository(dataStore: JetGamesPreferencesDataStore): SettingsRepository =
        SettingsRepositoryImpl(dataStore = dataStore)
}