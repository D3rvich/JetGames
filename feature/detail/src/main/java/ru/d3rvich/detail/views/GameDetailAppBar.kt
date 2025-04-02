package ru.d3rvich.detail.views

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import ru.d3rvich.common.components.collapsing_appbar.CollapsingTopAppBar
import ru.d3rvich.core.ui.theme.JetGamesTheme
import ru.d3rvich.detail.R

/**
 * Created by Ilya Deryabin at 13.03.2024
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GameDetailAppBar(
    modifier: Modifier = Modifier,
    title: String,
    imageUrl: String?,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
    isFavorite: Boolean,
    onBackClicked: () -> Unit,
    onFavoriteChange: (Boolean) -> Unit,
) {
    val animateIconBackground by animateFloatAsState(
        targetValue = 0.5f * (1 - scrollBehavior.state.collapsedFraction),
        label = "iconBackground"
    )
    val expandedHeight = 320.dp
    CollapsingTopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = title,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis
            )
        },
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            NavigationIcon(alphaFraction = animateIconBackground, onBackClicked = onBackClicked)
        },
        actions = {
            Actions(
                alphaFraction = animateIconBackground,
                isFavorite = isFavorite,
                onFavoriteChange = onFavoriteChange
            )
        },
        expandedContent = {
            ExpandedContent(
                height = expandedHeight,
                imageUrl = imageUrl,
                title = title
            )
        },
        expandedHeight = expandedHeight,
    )
}

@Composable
private fun NavigationIcon(
    modifier: Modifier = Modifier,
    alphaFraction: Float,
    onBackClicked: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = onBackClicked,
        colors = IconButtonDefaults.iconButtonColors()
            .copy(containerColor = MaterialTheme.colorScheme.background.copy(alpha = alphaFraction))
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Default.ArrowBack,
            contentDescription = stringResource(R.string.navigate_back),
        )
    }
}

@Composable
private fun Actions(
    modifier: Modifier = Modifier,
    alphaFraction: Float,
    isFavorite: Boolean,
    onFavoriteChange: (Boolean) -> Unit
) {
    val backgroundColor =
        MaterialTheme.colorScheme.background.copy(alpha = alphaFraction)
    IconToggleButton(
        modifier = modifier,
        checked = isFavorite,
        onCheckedChange = { onFavoriteChange(!isFavorite) },
        colors = IconButtonDefaults.iconToggleButtonColors(
            checkedContainerColor = backgroundColor,
            containerColor = backgroundColor
        )
    ) {
        val tintAnimation by animateColorAsState(
            targetValue = if (isFavorite) Color.Red else LocalContentColor.current,
            label = "tint"
        )
        Icon(
            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
            tint = tintAnimation,
            contentDescription = if (isFavorite) stringResource(R.string.remove_from_favorites) else
                stringResource(R.string.add_to_favorites)
        )
    }
}

@Composable
private fun ExpandedContent(
    modifier: Modifier = Modifier,
    height: Dp,
    imageUrl: String?,
    title: String
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
    ) {
        if (imageUrl != null) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.BottomCenter),
                contentScale = ContentScale.Crop
            )
        }
        var backgroundHeight by remember { mutableStateOf(Dp.Unspecified) }
        val brush = Brush.verticalGradient(
            listOf(
                Color.Transparent,
                MaterialTheme.colorScheme.background.copy(0.9f)
            )
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .background(brush)
                .fillMaxWidth()
                .height(backgroundHeight + 24.dp)
        )
        val density = LocalDensity.current
        Text(
            text = title,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Horizontal))
                .padding(bottom = 8.dp, start = 8.dp)
                .onSizeChanged {
                    with(density) {
                        backgroundHeight = it.height.toDp()
                    }
                },
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun GameDetailAppBarPreview() {
    JetGamesTheme {
        GameDetailAppBar(
            title = "Game name",
            imageUrl = null,
            onBackClicked = {},
            isFavorite = false,
            onFavoriteChange = {}
        )
    }
}