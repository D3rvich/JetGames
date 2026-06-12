package ru.d3rvich.jetgames.navigation.main.home

import ru.d3rvich.feature.home.HomeViewModel

interface HomeComponent {
    val homeViewModel: HomeViewModel

    fun onGameClick(gameId: Int)

    fun onSettingsClick()
}