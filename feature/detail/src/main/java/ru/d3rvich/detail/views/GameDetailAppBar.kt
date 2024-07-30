package ru.d3rvich.detail.views

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.imageLoader
import ru.d3rvich.common.components.collapsing_appbar.CollapsingTitle
import ru.d3rvich.common.components.collapsing_appbar.CollapsingTopAppBar
import ru.d3rvich.common.components.collapsing_appbar.CollapsingTopAppBarScrollBehavior
import ru.d3rvich.common.components.collapsing_appbar.rememberCollapsingTopAppBarScrollBehavior
import ru.d3rvich.core.ui.theme.JetGamesTheme
import ru.d3rvich.detail.R

/**
 * Created by Ilya Deryabin at 13.03.2024
 */
@Composable
internal fun GameDetailAppBar(
    modifier: Modifier = Modifier,
    title: String,
    imageUrl: String?,
    imageLoader: ImageLoader,
    collapsingScrollBehavior: CollapsingTopAppBarScrollBehavior,
    isFavorite: Boolean,
    onBackClicked: () -> Unit,
    onFavoriteChange: (Boolean) -> Unit,
) {
    val animateIconBackground by animateFloatAsState(
        targetValue = 0.5f * (1 - collapsingScrollBehavior.state.collapsedFraction),
        label = "iconBackground"
    )
    CollapsingTopAppBar(
        modifier = modifier,
        scrollBehavior = collapsingScrollBehavior,
        collapsingTitle = CollapsingTitle.large(titleText = title),
        navigationIcon = {
            IconButton(
                onClick = onBackClicked,
                colors = IconButtonDefaults.iconButtonColors()
                    .copy(containerColor = MaterialTheme.colorScheme.background.copy(alpha = animateIconBackground))
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = "Navigate back",
                )
            }
        },
        actions = {
            val backgroundColor =
                MaterialTheme.colorScheme.background.copy(alpha = animateIconBackground)
            IconToggleButton(
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
        },
        expandedBackground = {
            if (imageUrl != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(360.dp)
                        .graphicsLayer {
                            val scale = 1 + lerp(
                                0.05f,
                                0f,
                                collapsingScrollBehavior.state.collapsedFraction
                            )
                            scaleX = scale
                            scaleY = scale
                        },
                    contentAlignment = Alignment.BottomCenter
                ) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = null,
                        imageLoader = imageLoader,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true, apiLevel = 33)
@Composable
private fun GameDetailAppBarPreview() {
    JetGamesTheme {
        GameDetailAppBar(
            title = "Game name",
            imageUrl = null,
            imageLoader = LocalContext.current.imageLoader,
            collapsingScrollBehavior = rememberCollapsingTopAppBarScrollBehavior(),
            onBackClicked = {},
            isFavorite = false,
            onFavoriteChange = {}
        )
    }
}