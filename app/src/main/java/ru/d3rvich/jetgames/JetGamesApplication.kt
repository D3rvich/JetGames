package ru.d3rvich.jetgames

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.asImage
import coil3.disk.DiskCache
import coil3.disk.directory
import coil3.memory.MemoryCache
import coil3.request.crossfade
import coil3.util.DebugLogger
import dagger.hilt.android.HiltAndroidApp
import ru.d3rvich.common.R
import timber.log.Timber

/**
 * Created by Ilya Deryabin at 31.01.2024
 */
@HiltAndroidApp
class JetGamesApplication : Application(), SingletonImageLoader.Factory {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader {
        return ImageLoader.Builder(context).apply {
            memoryCache {
                MemoryCache.Builder().maxSizePercent(context, 0.25).build()
            }
            diskCache {
                DiskCache.Builder()
                    .directory(context.cacheDir.resolve("image_cache"))
                    .maxSizePercent(0.02)
                    .build()
            }
            placeholder(
                context.getDrawable(R.drawable.ic_gamepad_24)?.asImage()
            )
            logger(DebugLogger())
            crossfade(true)
        }
            .build()
    }
}