package ru.d3rvich.jetgames.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import ru.d3rvich.core.data.di.DataModule

@Module(includes = [DataModule::class, FeatureModule::class, DomainModule::class])
@ComponentScan("ru.d3rvich.jetgames")
object AppModule