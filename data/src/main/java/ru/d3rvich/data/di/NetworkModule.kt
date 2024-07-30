package ru.d3rvich.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.d3rvich.data.BuildConfig
import ru.d3rvich.remote.JetGamesApiService
import javax.inject.Singleton

/**
 * Created by Ilya Deryabin at 05.02.2024
 */
@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Provides
    @Singleton
    fun provideJetGamesApi(): JetGamesApiService =
        JetGamesApiService(BuildConfig.API_KEY)
}