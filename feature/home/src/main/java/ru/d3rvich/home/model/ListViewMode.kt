package ru.d3rvich.home.model

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.d3rvich.home.R

/**
 * Created by Ilya Deryabin at 11.06.2024
 */
@Immutable
internal enum class ListViewMode(val iconResId: Int, val stringResId: Int) {
    Grid(R.drawable.ic_grid_view_24, R.string.list_view_mode_grid),
    Compact(R.drawable.ic_compact_view_24, R.string.list_view_mode_compact),
    Large(R.drawable.id_large_view_24, R.string.list_view_mode_large)
}

@Composable
internal fun rememberListViewModeProvider(context: Context = LocalContext.current): ListViewModeProvider =
    remember {
        ListViewModeProvider(context.applicationContext)
    }

@Stable
internal class ListViewModeProvider(context: Context) {

    private val _currentListViewMode = MutableStateFlow(DefaultListViewMode)
    val currentListViewMode: StateFlow<ListViewMode>
        get() = _currentListViewMode.asStateFlow()

    private val sharedPreferences =
        context.getSharedPreferences(SharedPreferencesKey, Context.MODE_PRIVATE)
            .also { preferences ->
                preferences.getString(ListViewModeKey, DefaultListViewMode.name)?.let { modeName ->
                    _currentListViewMode.value = ListViewMode.valueOf(modeName)
                }
            }

    fun setListViewMode(viewMode: ListViewMode) {
        _currentListViewMode.value = viewMode
        sharedPreferences.edit {
            putString(ListViewModeKey, viewMode.name)
        }
    }
}

private const val SharedPreferencesKey = "ListViewModeProvider_SharedPreferences"
private const val ListViewModeKey = "ListViewMode"

@Stable
private val DefaultListViewMode = ListViewMode.Compact