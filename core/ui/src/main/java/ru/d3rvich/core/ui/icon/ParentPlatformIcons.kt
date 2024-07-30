package ru.d3rvich.core.ui.icon

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import ru.d3rvich.core.domain.entities.ParentPlatformEntity
import ru.d3rvich.core.ui.R

object ParentPlatformIcons {

    @Stable
    val Windows: Painter
        @Composable get() = painterResource(id = R.drawable.ic_platform_windows)

    @Stable
    val Linux: Painter
        @Composable get() = painterResource(id = R.drawable.ic_platform_linux)

    @Stable
    val Playstation: Painter
        @Composable get() = painterResource(id = R.drawable.ic_platform_playstation)

    @Stable
    val Xbox: Painter
        @Composable get() = painterResource(id = R.drawable.ic_platform_xbox)

    @Stable
    val Apple: Painter
        @Composable get() = painterResource(id = R.drawable.ic_platform_apple)

    @Stable
    val Android: Painter
        @Composable get() = painterResource(id = R.drawable.ic_platform_android)

    @Stable
    val Nintendo: Painter
        @Composable get() = painterResource(id = R.drawable.ic_platform_nintendo)
}

@Composable
fun ParentPlatformEntity.tryFindIcon(): Painter? = name.lowercase().let { nameLowerCase ->
    when {
        nameLowerCase.contains("xbox") -> ParentPlatformIcons.Xbox
        nameLowerCase.contains("playstation") -> ParentPlatformIcons.Playstation
        nameLowerCase.contains("pc") -> ParentPlatformIcons.Windows
        nameLowerCase.contains("linux") -> ParentPlatformIcons.Linux
        nameLowerCase.contains("apple") -> ParentPlatformIcons.Apple
        nameLowerCase.contains("android") -> ParentPlatformIcons.Android
        nameLowerCase.contains("nintendo") -> ParentPlatformIcons.Nintendo
        else -> null
    }
}