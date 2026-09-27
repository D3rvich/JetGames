package ru.d3rvich.feature.detail.ui.views

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.windowInsetsTopHeight
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import ru.d3rvich.core.ui.components.topappbar.CollapsingTopAppBar
import ru.d3rvich.core.ui.theme.JetGamesTheme
import ru.d3rvich.feature.detail.ui.R
import ru.d3rvich.common.R as uiR

/**
 * Created by Ilya Deryabin at 13.03.2024
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
internal fun GameDetailAppBar(
    title: String,
    imageUrl: String?,
    isFavorite: Boolean,
    onBackClicked: () -> Unit,
    onFavoriteChange: (Boolean) -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
) {
    val originalIconBackground = MaterialTheme.colorScheme.surface
    val iconBackgroundColor = {
        val fraction = scrollBehavior.state.collapsedFraction
        originalIconBackground.copy(alpha = 0.5f * (1f - fraction))
    }
    val density = LocalDensity.current
    val statusBarInsets = WindowInsets.statusBars.only(WindowInsetsSides.Top)
    val statusBarHeight = remember(density, statusBarInsets) {
        with(density) {
            statusBarInsets.getTop(density).toDp()
        }
    }
    val expandedHeight = INIT_EXPANDED_HEIGHT + statusBarHeight
    CollapsingTopAppBar(
        modifier = modifier,
        collapsedTitle = {
            Text(
                text = title,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis
            )
        },
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            NavigationIcon(
                iconBackgroundColor = iconBackgroundColor,
                onBackClicked = onBackClicked
            )
        },
        actions = {
            Actions(
                isFavorite = isFavorite,
                iconBackgroundColor = iconBackgroundColor,
                onFavoriteChange = onFavoriteChange
            )
        },
        expandedContent = {
            ExpandedContent(
                imageUrl = imageUrl,
                title = title,
                scrollBehavior = scrollBehavior,
            )
        },
        expandedHeight = expandedHeight,
    )
}

@Composable
private fun NavigationIcon(
    iconBackgroundColor: () -> Color,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        modifier = modifier.drawBehind {
            drawCircle(iconBackgroundColor(), radius = size.minDimension / 2.5f)
        },
        onClick = onBackClicked,
        colors = IconButtonDefaults.iconButtonColors().copy(containerColor = Color.Transparent)
    ) {
        Icon(
            painter = painterResource(uiR.drawable.arrow_back_24px),
            contentDescription = stringResource(R.string.navigate_back),
        )
    }
}

@Composable
private fun Actions(
    isFavorite: Boolean,
    iconBackgroundColor: () -> Color,
    onFavoriteChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    IconToggleButton(
        modifier = modifier.drawBehind {
            drawCircle(iconBackgroundColor(), radius = size.minDimension / 2.5f)
        },
        checked = isFavorite,
        onCheckedChange = { onFavoriteChange(!isFavorite) },
        colors = IconButtonDefaults.iconToggleButtonColors(
            checkedContainerColor = Color.Transparent,
            containerColor = Color.Transparent
        )
    ) {
        val tintAnimation by animateColorAsState(
            targetValue = if (isFavorite) Color.Red else LocalContentColor.current,
            label = "tint"
        )
        Icon(
            painter = painterResource(
                if (isFavorite) uiR.drawable.favorite_filled_24px
                else uiR.drawable.favorite_24px
            ),
            tint = tintAnimation,
            contentDescription = if (isFavorite) stringResource(R.string.remove_from_favorites) else
                stringResource(R.string.add_to_favorites)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExpandedContent(
    title: String,
    imageUrl: String?,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        if (imageUrl != null) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        val backgroundColor = MaterialTheme.colorScheme.background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .background(backgroundColor.copy(0.5f))
                .align(Alignment.TopStart)
        )
        val brush = Brush.verticalGradient(
            listOf(
                Color.Transparent,
                backgroundColor
            )
        )
        Text(
            text = title,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .graphicsLayer {
                    translationY = scrollBehavior.state.heightOffset
                }
                .drawBehind {
                    drawRect(brush)
                }
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))
                .padding(start = 8.dp, bottom = 8.dp, top = 32.dp),
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

private val INIT_EXPANDED_HEIGHT = 320.dp

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
            onFavoriteChange = {},
            scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
        )
    }
}