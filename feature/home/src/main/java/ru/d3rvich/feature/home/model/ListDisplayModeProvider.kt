package ru.d3rvich.feature.home.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam
import ru.d3rvich.core.domain.model.ListDisplayOption
import ru.d3rvich.core.domain.repositories.UserPreferencesRepository
import kotlin.time.Duration.Companion.milliseconds

@Factory
internal class ListDisplayModeProvider(
    @InjectedParam scope: CoroutineScope,
    private val cacheProvider: CacheProvider
) {
    val listDisplayModeFlow: StateFlow<ListDisplayMode?> = cacheProvider.getListDisplayMode()
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5000.milliseconds),
            initialValue = null
        )

    suspend fun setListViewMode(displayMode: ListDisplayMode) {
        cacheProvider.setListDisplayMode(displayMode)
    }

    interface CacheProvider {
        fun getListDisplayMode(): Flow<ListDisplayMode>

        suspend fun setListDisplayMode(mode: ListDisplayMode)
    }
}

@Factory(binds = [ListDisplayModeProvider.CacheProvider::class])
internal class CacheProviderImpl(private val userPreferencesRepository: UserPreferencesRepository) :
    ListDisplayModeProvider.CacheProvider {
    override fun getListDisplayMode(): Flow<ListDisplayMode> =
        userPreferencesRepository.getListDisplayOption()
            .map(ListDisplayOption::toListDisplayMode)

    override suspend fun setListDisplayMode(mode: ListDisplayMode) {
        userPreferencesRepository.setListDisplayOption(mode.toListDisplayOption())
    }
}