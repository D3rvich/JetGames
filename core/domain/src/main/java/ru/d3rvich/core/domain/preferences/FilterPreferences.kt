package ru.d3rvich.core.domain.preferences

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.annotation.Single
import ru.d3rvich.core.model.FilterPreferencesBody
import javax.inject.Inject

/**
 * Created by Ilya Deryabin at 07.03.2024
 */
@Single
class FilterPreferences @Inject constructor() {
    val filterPreferencesFlow: StateFlow<FilterPreferencesBody>
        field = MutableStateFlow(FilterPreferencesBody.default())

    fun applyFilterPreferences(body: FilterPreferencesBody) {
        filterPreferencesFlow.value = body
    }

    fun reset() {
        filterPreferencesFlow.value = FilterPreferencesBody.default()
    }
}
