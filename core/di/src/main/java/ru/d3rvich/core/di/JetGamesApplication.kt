package ru.d3rvich.core.di

import org.koin.core.annotation.KoinApplication
import ru.d3rvich.core.data.di.DataModule
import ru.d3rvich.core.di.modules.DomainModule
import ru.d3rvich.core.di.modules.FeatureModule

@KoinApplication(modules = [DataModule::class, FeatureModule::class, DomainModule::class])
object JetGamesApplication