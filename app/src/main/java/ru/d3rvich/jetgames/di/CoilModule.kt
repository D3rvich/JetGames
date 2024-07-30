package ru.d3rvich.jetgames.di

import android.content.Context
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.util.DebugLogger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.d3rvich.common.R

/**
 * Created by Ilya Deryabin at 16.02.2024
 */
@Module
@InstallIn(SingletonComponent::class)
object CoilModule {

    @Provides
    fun provideMemoryCache(@ApplicationContext context: Context): MemoryCache =
        MemoryCache.Builder(context).maxSizePercent(0.25).build()

    @Provides
    fun provideDistCache(@ApplicationContext context: Context): DiskCache =
        DiskCache.Builder()
            .directory(context.cacheDir.resolve("image_cache"))
            .maxSizePercent(0.2)
            .build()

    @Provides
    fun provideImageLoader(
        @ApplicationContext context: Context,
        memoryCache: MemoryCache,
        diskCache: DiskCache
    ): ImageLoader =
        ImageLoader.Builder(context.applicationContext).apply {
            placeholder(R.drawable.ic_gamepad_24)
            memoryCache(memoryCache)
            diskCache(diskCache)
            logger(DebugLogger())
            crossfade(true)
        }.build()
}