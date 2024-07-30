package ru.d3rvich.jetgames.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
import ru.d3rvich.common.navigation.Screens
import ru.d3rvich.home.R

/**
 * Created by Ilya Deryabin at 11.05.2024
 */
enum class TopLevelDestination(
    val route: Any,
    val labelResId: Int,
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector = unselectedIcon,
) {
    Home(
        route = Screens.Home,
        labelResId = R.string.feature_home_title,
        unselectedIcon = Icons.Outlined.Home,
        selectedIcon = Icons.Filled.Home
    ),
    Browse(
        route = Screens.Browse,
        labelResId = ru.d3rvich.jetgames.R.string.feature_browse_title,
        unselectedIcon = Icons.Outlined.Search
    ),
    Favorites(
        route = Screens.Favorites,
        labelResId = ru.d3rvich.favorites.R.string.feature_favorites_title,
        unselectedIcon = Icons.Outlined.FavoriteBorder,
        selectedIcon = Icons.Filled.Favorite
    )
}