package ru.d3rvich.feature.filter.impl

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import org.koin.core.annotation.KoinViewModel
import ru.d3rvich.core.domain.preferences.FilterPreferences
import ru.d3rvich.core.domain.usecases.GetGenresUseCase
import ru.d3rvich.core.domain.usecases.GetPlatformsUseCase
import ru.d3rvich.feature.filter.impl.store.FilterStore
import ru.d3rvich.feature.filter.impl.store.FilterStoreFactory
import kotlin.time.Duration.Companion.milliseconds

/**
 * Created by Ilya Deryabin at 29.02.2024
 */
@Stable
@KoinViewModel
class FilterViewModel(
    private val filterPreferences: FilterPreferences,
    private val getPlatformsUseCase: GetPlatformsUseCase,
    private val getGenresUseCase: GetGenresUseCase,
    storeFactory: StoreFactory = DefaultStoreFactory(),
) : ViewModel() {

    private val store: FilterStore = FilterStoreFactory(
        storeFactory = storeFactory,
        filterPreferences = filterPreferences,
        getPlatformsUseCase = getPlatformsUseCase,
        getGenresUseCase = getGenresUseCase
    ).create()

    internal val uiState =
        store.stateFlow(viewModelScope, SharingStarted.WhileSubscribed(5000.milliseconds))

    internal val labels = store.labels

    internal fun obtainIntent(intent: FilterStore.Intent) {
        store.accept(intent)
    }
}