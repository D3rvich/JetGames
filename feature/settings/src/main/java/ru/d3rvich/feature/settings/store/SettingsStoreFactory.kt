package ru.d3rvich.feature.settings.store

import android.os.Build
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import ru.d3rvich.core.domain.model.UserPreferences
import ru.d3rvich.core.domain.repositories.UserPreferencesRepository

@Factory
internal class SettingsStoreFactory(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val storeFactory: StoreFactory = DefaultStoreFactory()
) {
    fun create(): SettingsStore = object : SettingsStore,
        Store<SettingsStore.Intent, SettingsStore.State, Nothing> by storeFactory.create<SettingsStore.Intent, Unit, Message, SettingsStore.State, Nothing>(
            name = "SettingsStore",
            initialState = SettingsStore.State.Loading,
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = coroutineExecutorFactory {
                onAction<Unit> {
                    userPreferencesRepository.getUserPreferences().onEach { userPreferences ->
                        dispatch(Message.UpdateUserPreferences(userPreferences))
                    }.launchIn(this)
                }

                onIntent<SettingsStore.Intent.ColorModeSelected> { intent ->
                    launch {
                        userPreferencesRepository.setCurrentColorMode(intent.colorMode)
                    }
                }

                onIntent<SettingsStore.Intent.ThemeTypeSelected> { intent ->
                    launch {
                        userPreferencesRepository.setCurrentTheme(intent.themeType)
                    }
                }
            },
            reducer = { message ->
                when (message) {
                    is Message.UpdateUserPreferences -> {
                        val isDynamicThemeSupported = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
                        with(message.userPreferences) {
                            SettingsStore.State.Settings(
                                themeType = theme,
                                colorMode = colorMode,
                                inDynamicColorSupported = isDynamicThemeSupported
                            )
                        }
                    }
                }
            }
        ) {}

    private sealed interface Message {
        data class UpdateUserPreferences(val userPreferences: UserPreferences) : Message
    }
}