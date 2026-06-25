package ru.d3rvich.core.di.modules

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import ru.d3rvich.core.data.di.DataModule

@Module(includes = [DomainModule::class, DataModule::class])
@ComponentScan(
    "ru.d3rvich.feature.*.integration",
    "ru.d3rvich.feature.*.store",
    "ru.d3rvich.feature"
)
object FeatureModule