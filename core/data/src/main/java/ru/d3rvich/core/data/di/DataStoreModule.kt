package ru.d3rvich.core.data.di

import android.content.Context
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import ru.d3rvich.core.datastore.JetGamesPreferencesDataStore

@Module
object DataStoreModule {

    @Single(createdAtStart = true)
    fun datastore(context: Context) = JetGamesPreferencesDataStore(context)
}