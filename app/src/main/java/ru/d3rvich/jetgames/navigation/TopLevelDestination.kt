package ru.d3rvich.jetgames.navigation

import ru.d3rvich.common.navigation.Screens
import ru.d3rvich.home.R
import ru.d3rvich.common.R as commonR

/**
 * Created by Ilya Deryabin at 11.05.2024
 */
enum class TopLevelDestination(
    val route: Any,
    val labelResId: Int,
    val unselectedIconResId: Int,
    val selectedIconResId: Int = unselectedIconResId,
) {
    Home(
        route = Screens.Home,
        labelResId = R.string.feature_home_title,
        unselectedIconResId = commonR.drawable.home_24px,
        selectedIconResId = commonR.drawable.home_filled_24px
    ),
    Browse(
        route = Screens.Browse,
        labelResId = ru.d3rvich.jetgames.R.string.feature_browse_title,
        unselectedIconResId = commonR.drawable.search_24px
    ),
    Favorites(
        route = Screens.Favorites,
        labelResId = ru.d3rvich.favorites.R.string.feature_favorites_title,
        unselectedIconResId = commonR.drawable.favorite_24px,
        selectedIconResId = commonR.drawable.favorite_filled_24px
    )
}