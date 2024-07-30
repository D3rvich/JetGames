package ru.d3rvich.detail.views

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.imageLoader
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import ru.d3rvich.common.components.CollapsingText
import ru.d3rvich.common.components.collapsing_appbar.rememberCollapsingTopAppBarScrollBehavior
import ru.d3rvich.core.domain.entities.GameDetailEntity
import ru.d3rvich.core.domain.entities.ParentPlatformEntity
import ru.d3rvich.core.domain.entities.RatingEntity
import ru.d3rvich.core.ui.icon.RatingType
import ru.d3rvich.core.ui.icon.findWrapper
import ru.d3rvich.core.ui.icon.textIcon
import ru.d3rvich.core.ui.icon.tryFindIcon
import ru.d3rvich.core.ui.theme.DarkBlue
import ru.d3rvich.core.ui.theme.Purple
import ru.d3rvich.detail.R
import ru.d3rvich.detail.model.GameDetailUiModel
import ru.d3rvich.detail.model.ScreenshotsUiState
import ru.d3rvich.detail.model.toGameDetailUiModel

/**
 * Created by Ilya Deryabin at 11.03.2024
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun GameDetailView(
    modifier: Modifier = Modifier,
    detail: GameDetailUiModel,
    screenshotsState: ScreenshotsUiState,
    isFavorite: Boolean,
    imageLoader: ImageLoader,
    onFavoriteChange: (Boolean) -> Unit,
    onScreenshotClicked: (selectedItem: Int) -> Unit,
    onBackClicked: () -> Unit,
) {
    val collapsingScrollBehavior = rememberCollapsingTopAppBarScrollBehavior()
    Scaffold(
        modifier = modifier,
        topBar = {
            GameDetailAppBar(
                title = "${detail.name} ${
                    detail.ratings?.maxByOrNull { it.percent }?.findWrapper()?.textIcon ?: ""
                }",
                imageUrl = detail.imageUrl,
                imageLoader = imageLoader,
                collapsingScrollBehavior = collapsingScrollBehavior,
                onBackClicked = onBackClicked,
                isFavorite = isFavorite,
                onFavoriteChange = onFavoriteChange
            )
        },
        contentWindowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .nestedScroll(collapsingScrollBehavior.nestedScrollConnection),
            verticalArrangement = Arrangement.spacedBy(32.dp),
            contentPadding = PaddingValues(
                top = 20.dp,
                bottom = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
            )
        ) {
            if (detail.metacritic != null || detail.rating != null) {
                item {
                    RatingsView(metacritic = detail.metacritic, rating = detail.rating)
                }
            }
            if (!detail.ratings.isNullOrEmpty()) {
                item {
                    RatingDetailView(ratings = detail.ratings)
                }
            }
            if (!detail.genres.isNullOrEmpty()) {
                item {
                    GameDetailItem(title = stringResource(R.string.genres)) {
                        FlowRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            detail.genres.forEach { genre ->
                                val brush = Brush.horizontalGradient(listOf(Purple, DarkBlue))
                                Text(
                                    text = genre.name,
                                    modifier = Modifier
                                        .border(
                                            width = 2.dp, brush = brush, shape = CircleShape
                                        )
                                        .padding(vertical = 4.dp, horizontal = 8.dp)
                                )
                            }
                        }
                    }
                }
            }
            if (!detail.parentPlatforms.isNullOrEmpty()) {
                item {
                    GameDetailItem(title = stringResource(R.string.platforms)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            detail.parentPlatforms.forEach { platform ->
                                platform.tryFindIcon()?.let { iconPainter ->
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            painter = iconPainter,
                                            contentDescription = platform.name
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(text = platform.name)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            item {
                GameDetailItem(title = stringResource(R.string.screenshots)) {
                    ScreenshotsView(
                        screenshotsState = screenshotsState,
                        imageLoader = imageLoader,
                        onItemClicked = onScreenshotClicked,
                    )
                }
            }
            if (detail.released != null) {
                item {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = stringResource(R.string.release_date),
                                style = MaterialTheme.typography.titleLarge
                            )
                            val dateFormat = LocalDate.Format {
                                monthName(MonthNames.ENGLISH_ABBREVIATED)
                                char(' ')
                                dayOfMonth()
                                chars(", ")
                                year()
                            }
                            Text(
                                text = detail.released.format(dateFormat),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        HorizontalDivider()
                    }
                }
            }
            if (detail.description != null) {
                item {
                    GameDetailItem(
                        title = stringResource(R.string.about),
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        CollapsingText(
                            text = detail.description,
                            collapsedMaxLines = 5,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GameDetailItem(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = title,
            style = MaterialTheme.typography.titleLarge
        )
        HorizontalDivider(
            modifier = Modifier
                .padding(bottom = 12.dp)
        )
        content()
    }
}

@Preview(showBackground = true)
@Composable
private fun GameDetailViewPreview() {
    val ratings = RatingType.entries.map { rating ->
        RatingEntity(
            id = rating.originalId,
            title = rating.name,
            count = 100,
            percent = 25f
        )
    }
    val detail = GameDetailEntity(
        id = 0,
        name = "Game name",
        description = "Description",
        screenshotCount = 0,
        screenshots = emptyList(),
        released = LocalDate(2015, 11, 17),
        metacritic = 69,
        imageUrl = null,
        genres = null,
        rating = 4.4f,
        parentPlatforms = listOf(
            ParentPlatformEntity(0, "PC"),
            ParentPlatformEntity(1, "Xbox One")
        ),
        ratings = ratings
    )
    val context = LocalContext.current
    GameDetailView(
        detail = detail.toGameDetailUiModel(),
        screenshotsState = ScreenshotsUiState.NoScreenshots,
        isFavorite = false,
        imageLoader = context.imageLoader,
        onFavoriteChange = {},
        onScreenshotClicked = {},
        onBackClicked = {},
    )
}