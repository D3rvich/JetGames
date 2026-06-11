package ru.d3rvich.jetgames.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(includes = [DomainModule::class])
@ComponentScan("ru.d3rvich.feature")
object FeatureModule