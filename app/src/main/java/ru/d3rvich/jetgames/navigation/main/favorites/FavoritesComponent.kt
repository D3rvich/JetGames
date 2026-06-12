package ru.d3rvich.jetgames.navigation.main.favorites

import ru.d3rvich.feature.favorites.FavoritesViewModel

interface FavoritesComponent {
    val favoritesViewModel: FavoritesViewModel

    fun onGameClick(gameId: Int)

    fun inSettingsClick()
}