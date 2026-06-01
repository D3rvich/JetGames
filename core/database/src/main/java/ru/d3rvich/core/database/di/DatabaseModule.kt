package ru.d3rvich.core.database.di

import android.content.Context
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import ru.d3rvich.core.database.JetGamesDatabase

@Module
object DatabaseModule {

    @Single(binds = [JetGamesDatabase::class], createdAtStart = true)
    fun database(context: Context): JetGamesDatabase =
        JetGamesDatabase(applicationContext = context)
}