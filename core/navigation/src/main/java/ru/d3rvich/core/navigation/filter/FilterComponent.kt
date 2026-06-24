package ru.d3rvich.core.navigation.filter

import ru.d3rvich.feature.filter.FilterViewModel

interface FilterComponent {
    val filterViewModel: FilterViewModel

    fun onBackClick()
}