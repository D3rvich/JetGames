package ru.d3rvich.jetgames.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(includes = [DomainModule::class])
@ComponentScan(
    "ru.d3rvich.browse",
    "ru.d3rvich.detail",
    "ru.d3rvich.favorites",
    "ru.d3rvich.filter",
    "ru.d3rvich.home",
    "ru.d3rvich.settings"
)
object FeatureModule