package ru.d3rvich.data.model

import android.content.Context
import androidx.core.content.edit

internal class SynchronizeTimeHolder(context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    var lastSyncPlatformsTimestamp: Long
        get() = sharedPreferences.getLong(PLATFORMS_SYNCHRONIZE_TIME, DEFAULT_TIMESTAMP)
        set(value) = sharedPreferences.edit {
            putLong(PLATFORMS_SYNCHRONIZE_TIME, value)
        }

    var lastSyncGenresTimestamp: Long
        get() = sharedPreferences.getLong(GENRES_SYNCHRONIZE_TIME, DEFAULT_TIMESTAMP)
        set(value) = sharedPreferences.edit {
            putLong(GENRES_SYNCHRONIZE_TIME, value)
        }

    internal companion object {
        const val DEFAULT_TIMESTAMP = -1L
        private const val SHARED_PREFERENCES_NAME = "SynchronizeTimeHolder"
        private const val PLATFORMS_SYNCHRONIZE_TIME = "platformsSynchronize"
        private const val GENRES_SYNCHRONIZE_TIME = "genresSynchronize"
    }
}