package ru.d3rvich.jetgames

import android.app.Application
import androidx.appcompat.content.res.AppCompatResources
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.asImage
import coil3.disk.DiskCache
import coil3.disk.directory
import coil3.memory.MemoryCache
import coil3.request.crossfade
import coil3.util.DebugLogger
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.plugin.module.dsl.modules
import ru.d3rvich.core.di.initKoin
import timber.log.Timber

/**
 * Created by Ilya Deryabin at 31.01.2024
 */
class JetGamesAndroidApplication : Application(), SingletonImageLoader.Factory {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        initKoin {
            modules(ViewModelModule::class)
            androidLogger()
            androidContext(this@JetGamesAndroidApplication)
        }
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader =
        ImageLoader.Builder(context).apply {
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
                AppCompatResources.getDrawable(
                    this@JetGamesAndroidApplication,
                    R.drawable.ic_gamepad_24
                )?.asImage()
            )
            logger(DebugLogger())
            crossfade(true)
        }
            .build()
}