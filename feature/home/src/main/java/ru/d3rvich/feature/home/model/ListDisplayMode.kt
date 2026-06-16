package ru.d3rvich.feature.home.model

import ru.d3rvich.core.domain.model.ListDisplayOption
import ru.d3rvich.feature.home.R

enum class ListDisplayMode(val iconResId: Int, val stringResId: Int) {
    Grid(R.drawable.ic_grid_view_24, R.string.list_view_mode_grid),
    Compact(R.drawable.ic_compact_view_24, R.string.list_view_mode_compact),
    Large(R.drawable.id_large_view_24, R.string.list_view_mode_large)
}

internal fun ListDisplayOption.toListDisplayMode(): ListDisplayMode = when (this) {
    ListDisplayOption.Grid -> ListDisplayMode.Grid
    ListDisplayOption.Compact -> ListDisplayMode.Compact
    ListDisplayOption.Large -> ListDisplayMode.Large
}

internal fun ListDisplayMode.toListDisplayOption(): ListDisplayOption = when (this) {
    ListDisplayMode.Grid -> ListDisplayOption.Grid
    ListDisplayMode.Compact -> ListDisplayOption.Compact
    ListDisplayMode.Large -> ListDisplayOption.Large
}