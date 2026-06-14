package ru.d3rvich.feature.home.model

import android.content.Context
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.annotation.Factory

@Factory
class ListDisplayModeProvider(context: Context) {

    private val _currentListViewMode = MutableStateFlow<ListDisplayMode?>(null)

    val listDisplayModeFlow: StateFlow<ListDisplayMode?>
        get() = _currentListViewMode.asStateFlow()

    private val sharedPreferences =
        context.getSharedPreferences(SharedPreferencesKey, Context.MODE_PRIVATE)
            .also { preferences ->
                preferences.getString(ListViewModeKey, DefaultListDisplayMode.name)
                    ?.let { modeName ->
                        _currentListViewMode.value = ListDisplayMode.valueOf(modeName)
                    }
            }

    fun setListViewMode(viewMode: ListDisplayMode) {
        _currentListViewMode.value = viewMode
        sharedPreferences.edit {
            putString(ListViewModeKey, viewMode.name)
        }
    }
}

private const val SharedPreferencesKey = "ListViewModeProvider_SharedPreferences"
private const val ListViewModeKey = "ListViewMode"

private val DefaultListDisplayMode = ListDisplayMode.Compact