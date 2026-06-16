package ru.d3rvich.feature.home

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.SharingStarted
import org.koin.core.annotation.KoinViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import ru.d3rvich.core.domain.preferences.FilterPreferences
import ru.d3rvich.core.domain.usecases.GetGamesUseCase
import ru.d3rvich.feature.home.model.ListDisplayModeProvider
import ru.d3rvich.feature.home.store.HomeStore
import ru.d3rvich.feature.home.store.HomeStoreFactory

/**
 * Created by Ilya Deryabin at 31.01.2024
 */
@OptIn(FlowPreview::class)
@Stable
@KoinViewModel
class HomeViewModel(
    getGamesUseCase: GetGamesUseCase,
    filterPreferences: FilterPreferences,
    storeFactory: StoreFactory = DefaultStoreFactory(),
) : ViewModel(), KoinComponent {

    private val listDisplayModeProvider: ListDisplayModeProvider by inject {
        parametersOf(viewModelScope)
    }

    private val store: HomeStore =
        HomeStoreFactory(
            storeFactory = storeFactory,
            getGamesUseCase = getGamesUseCase,
            filterPreferences = filterPreferences,
            listDisplayModeProvider = listDisplayModeProvider,
        ).create()

    internal val uiState = store.stateFlow(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000)
    )

    internal fun obtainIntent(intent: HomeStore.Intent) {
        store.accept(intent)
    }
}