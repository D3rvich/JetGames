package ru.d3rvich.feature.home.ui.model

import ru.d3rvich.core.model.ListDisplayOption
import ru.d3rvich.feature.home.ui.R

internal val ListDisplayOption.iconResId: Int
    get() = when (this) {
        ListDisplayOption.Grid -> R.drawable.ic_grid_view_24
        ListDisplayOption.Compact -> R.drawable.ic_compact_view_24
        ListDisplayOption.Large -> R.drawable.id_large_view_24
    }

internal val ListDisplayOption.stringResId: Int
    get() = when (this) {
        ListDisplayOption.Grid -> R.string.list_view_mode_grid
        ListDisplayOption.Compact -> R.string.list_view_mode_compact
        ListDisplayOption.Large -> R.string.list_view_mode_large
    }