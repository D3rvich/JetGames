package ru.d3rvich.jetgames

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * Created by Ilya Deryabin at 31.01.2024
 */
@HiltAndroidApp
class JetGamesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}