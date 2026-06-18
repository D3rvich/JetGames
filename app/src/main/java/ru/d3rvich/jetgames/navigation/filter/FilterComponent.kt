package ru.d3rvich.jetgames.navigation.filter

import ru.d3rvich.feature.filter.FilterViewModel

interface FilterComponent {
    val filterViewModel: FilterViewModel

    fun onBackClick()
}