package ru.d3rvich.core.navigation

import ru.d3rvich.core.navigation.main.MainComponent
import ru.d3rvich.common.R as commonR
import ru.d3rvich.feature.home.R as homeR
import ru.d3rvich.feature.browse.ui.R as browseR
import ru.d3rvich.feature.favorites.R as favoritesR

enum class TabItems(
    val labelResId: Int,
    val unselectedIconResId: Int,
    val selectedIconResId: Int = unselectedIconResId,
) {
    Home(
        labelResId = homeR.string.feature_home_title,
        unselectedIconResId = commonR.drawable.home_24px,
        selectedIconResId = commonR.drawable.home_filled_24px
    ),
    Browse(
        labelResId = browseR.string.browse,
        unselectedIconResId = commonR.drawable.search_24px
    ),
    Favorites(
        labelResId = favoritesR.string.feature_favorites_title,
        unselectedIconResId = commonR.drawable.favorite_24px,
        selectedIconResId = commonR.drawable.favorite_filled_24px
    )
}

fun MainComponent.Child.toTabItem(): TabItems = when (this) {
    is MainComponent.Child.Browse -> TabItems.Browse
    is MainComponent.Child.Favorites -> TabItems.Favorites
    is MainComponent.Child.Home -> TabItems.Home
}

fun MainComponent.performClick(destination: TabItems) {
    when (destination) {
        TabItems.Home -> onHomeClick()
        TabItems.Browse -> onBrowseClick()
        TabItems.Favorites -> onFavoritesCLick()
    }
}