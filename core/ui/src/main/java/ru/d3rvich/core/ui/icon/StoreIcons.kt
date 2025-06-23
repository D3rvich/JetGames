package ru.d3rvich.core.ui.icon

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import ru.d3rvich.core.domain.entities.StoreEntity
import ru.d3rvich.core.ui.R

object StoreIcons {

    @Stable
    val Steam: Painter
        @Composable get() = painterResource(id = R.drawable.ic_store_steam)

    @Stable
    val EpicGames: Painter
        @Composable get() = painterResource(id = R.drawable.ic_store_epic_games)

    @Stable
    val GOG: Painter
        @Composable get() = painterResource(id = R.drawable.ic_store_gog)

    @Stable
    val PlayStationStore: Painter
        @Composable get() = painterResource(id = R.drawable.ic_platform_playstation)

    @Stable
    val Microsoft: Painter
        @Composable get() = painterResource(id = R.drawable.ic_platform_xbox)

    @Stable
    val AppStore: Painter
        @Composable get() = painterResource(id = R.drawable.ic_platform_apple)

    @Stable
    val GooglePlay: Painter
        @Composable get() = painterResource(id = R.drawable.ic_platform_android)

    @Stable
    val ItchIO: Painter
        @Composable get() = painterResource(id = R.drawable.ic_store_itch)

    @Stable
    val NintendoStore: Painter
        @Composable get() = painterResource(id = R.drawable.ic_platform_nintendo)
}

@Composable
fun StoreEntity.tryFindIcon(): Painter? =
    name.lowercase().let { nameLowercase ->
        when {
            nameLowercase.contains("steam") -> StoreIcons.Steam
            nameLowercase.contains("epic games") -> StoreIcons.EpicGames
            nameLowercase.contains("gog") -> StoreIcons.GOG
            nameLowercase.contains("playstation store") -> StoreIcons.PlayStationStore
            nameLowercase.contains("xbox store") || nameLowercase.contains("xbox 360 store") -> StoreIcons.Microsoft
            nameLowercase.contains("app store") -> StoreIcons.AppStore
            nameLowercase.contains("google play") -> StoreIcons.GooglePlay
            nameLowercase.contains("itch.io") -> StoreIcons.ItchIO
            nameLowercase.contains("nintendo store") -> StoreIcons.NintendoStore
            else -> null
        }
    }