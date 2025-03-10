package ru.d3rvich.home.model

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
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
internal fun rememberListViewModeProvider(
    context: Context = LocalContext.current,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
): ListViewModeProvider =
    remember {
        ListViewModeProvider(context = context.applicationContext, coroutineScope = coroutineScope)
    }

private object DataStoreHolder {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(DataStoreName)
}

@Stable
internal class ListViewModeProvider(
    private val context: Context,
    private val coroutineScope: CoroutineScope
) {

    val listViewMode =
        with(DataStoreHolder) {
            context.dataStore.data.map { preferences -> preferences[ListViewModeKey] }
                .map { raw ->
                    if (raw.isNullOrEmpty()) DefaultListViewMode else ListViewMode.valueOf(
                        raw
                    )
                }
                .stateIn(
                    scope = coroutineScope,
                    started = SharingStarted.WhileSubscribed(),
                    initialValue = DefaultListViewMode
                )


        }

    fun setListViewMode(viewMode: ListViewMode) {
        with(DataStoreHolder) {
            coroutineScope.launch {
                context.dataStore.edit { preferences ->
                    preferences[ListViewModeKey] = viewMode.name
                }
            }
        }
    }
}

private const val DataStoreName = "ListViewModeProvider"
private val ListViewModeKey = stringPreferencesKey("ListViewMode")

@Stable
private val DefaultListViewMode = ListViewMode.Compact