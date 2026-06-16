package ru.d3rvich.feature.settings

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import kotlinx.coroutines.flow.SharingStarted
import org.koin.core.annotation.KoinViewModel
import ru.d3rvich.core.domain.repositories.UserPreferencesRepository
import ru.d3rvich.feature.settings.store.SettingsStore
import ru.d3rvich.feature.settings.store.SettingsStoreFactory

@Stable
@KoinViewModel
class SettingsViewModel(
    userPreferencesRepository: UserPreferencesRepository,
    storeFactory: StoreFactory = DefaultStoreFactory()
) : ViewModel() {

    private val store = SettingsStoreFactory(storeFactory, userPreferencesRepository).create()

    internal val uiState = store.stateFlow(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000)
    )

    internal fun obtainIntent(intent: SettingsStore.Intent) {
        store.accept(intent)
    }
}