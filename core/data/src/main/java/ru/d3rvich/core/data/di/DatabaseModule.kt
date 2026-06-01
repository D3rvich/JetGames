package ru.d3rvich.core.data.di

import android.content.Context
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import ru.d3rvich.core.database.JetGamesDatabase

/**
 * Created by Ilya Deryabin at 02.04.2024
 */

@Module
object DatabaseModule {

    @Single(binds = [JetGamesDatabase::class], createdAtStart = true)
    fun database(context: Context): JetGamesDatabase {
        return JetGamesDatabase(context)
    }
}