package ru.d3rvich.data.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import ru.d3rvich.core.remote.di.NetworkModule

@Module(includes = [DataStoreModule::class, DatabaseModule::class, NetworkModule::class])
@ComponentScan("ru.d3rvich.data")
object DataModule