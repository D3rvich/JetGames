package ru.d3rvich.data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.d3rvich.data.repositoties.GamesRepositoryImpl
import ru.d3rvich.core.domain.repositories.GamesRepository
import ru.d3rvich.core.domain.repositories.GenresRepository
import ru.d3rvich.core.domain.repositories.PlatformsRepository
import ru.d3rvich.data.model.SynchronizeTimeHolder
import ru.d3rvich.data.paging.GamesPagingSource
import ru.d3rvich.data.repositoties.GenresRepositoryImpl
import ru.d3rvich.data.repositoties.PlatformsRepositoryImpl
import ru.d3rvich.database.JetGamesDatabase
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
        @ApplicationContext context: Context,
    ): PlatformsRepository = PlatformsRepositoryImpl(
        apiService = apiService,
        database = database,
        synchronizeTimeHolder = SynchronizeTimeHolder(context)
    )

    @Provides
    fun providesGenresRepository(
        apiService: JetGamesApiService,
        database: JetGamesDatabase,
        @ApplicationContext context: Context,
    ): GenresRepository = GenresRepositoryImpl(
        apiService = apiService,
        database = database,
        syncTimeHolder = SynchronizeTimeHolder(context)
    )
}