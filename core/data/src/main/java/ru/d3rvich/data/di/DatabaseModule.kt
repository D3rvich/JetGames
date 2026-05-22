package ru.d3rvich.data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.koin.core.annotation.Single
import ru.d3rvich.database.JetGamesDatabase
import javax.inject.Singleton

/**
 * Created by Ilya Deryabin at 02.04.2024
 */
@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): JetGamesDatabase {
        return JetGamesDatabase(context)
    }
}

@org.koin.core.annotation.Module
object KoinDatabaseModule {

    @Single
    fun database(context: Context): JetGamesDatabase = JetGamesDatabase(context)
}