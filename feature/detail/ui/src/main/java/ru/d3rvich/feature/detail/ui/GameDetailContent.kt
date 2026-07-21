package ru.d3rvich.feature.detail.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.d3rvich.common.components.DefaultErrorView
import ru.d3rvich.core.entity.ScreenshotEntity
import ru.d3rvich.feature.detail.api.LoadingModel
import ru.d3rvich.feature.detail.api.GameDetailComponent
import ru.d3rvich.feature.detail.ui.views.GameDetailView
import ru.d3rvich.feature.detail.ui.views.LoadingView

@Composable
fun GameDetailContent(component: GameDetailComponent, modifier: Modifier = Modifier) {
    val model by component.model.subscribeAsState()
    GameDetailContent(
        model = model,
        onRefresh = component::refresh,
        onFavoritesChange = component::setFavorites,
        onScreenshotsClicked = component::openScreenshots,
        onStoreClicked = component::openGameStore,
        onBackClicked = component::close,
    )
}

@Composable
private fun GameDetailContent(
    model: GameDetailComponent.Model,
    onRefresh: () -> Unit,
    onFavoritesChange: (Boolean) -> Unit,
    onScreenshotsClicked: (Int, List<ScreenshotEntity>) -> Unit,
    onStoreClicked: (String) -> Unit,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (model) {
        is GameDetailComponent.Model.Content -> GameDetailView(
            modifier = modifier,
            gameDetail = model.gameDetail,
            screenshots = model.screenshots,
            stores = model.stores,
            onFavoriteChange = onFavoritesChange,
            onBackClicked = onBackClicked,
            onScreenshotClicked = { selectedItem ->
                if (model.screenshots is LoadingModel.Success) {
                    onScreenshotsClicked(
                        selectedItem,
                        (model.screenshots as LoadingModel.Success<List<ScreenshotEntity>>).value
                    )
                }
            },
            onGameStoreSelected = onStoreClicked
        )

        is GameDetailComponent.Model.Error -> DefaultErrorView(
            message = model.throwable.localizedMessage ?: "error",
            onRefreshPressed = onRefresh
        )

        GameDetailComponent.Model.Loading -> LoadingView()
    }
}

