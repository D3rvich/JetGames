package ru.d3rvich.jetgames.di

import org.koin.core.annotation.KoinViewModel
import org.koin.core.annotation.Module
import ru.d3rvich.core.data.di.DataModule
import ru.d3rvich.core.domain.repositories.UserPreferencesRepository
import ru.d3rvich.jetgames.MainViewModel

@Module(includes = [DataModule::class, FeatureModule::class, DomainModule::class])
object AppModule {

    @KoinViewModel
    fun mainViewModel(userPreferencesRepository: UserPreferencesRepository): MainViewModel =
        MainViewModel(userPreferencesRepository = userPreferencesRepository)
}