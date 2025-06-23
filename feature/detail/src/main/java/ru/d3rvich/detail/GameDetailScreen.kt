package ru.d3rvich.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.d3rvich.common.components.DefaultErrorView
import ru.d3rvich.core.domain.entities.ScreenshotEntity
import ru.d3rvich.detail.model.GameDetailUiEvent
import ru.d3rvich.detail.model.GameDetailUiState
import ru.d3rvich.detail.model.ScreenshotsUiState
import ru.d3rvich.detail.model.toGameDetailUiModel
import ru.d3rvich.detail.views.GameDetailView
import ru.d3rvich.detail.views.LoadingView

/**
 * Created by Ilya Deryabin at 24.02.2024
 */
@Composable
fun GameDetailScreen(
    modifier: Modifier = Modifier,
    navigateToScreenshotScreen: (selectedItem: Int, screenshots: List<ScreenshotEntity>) -> Unit,
    navigateBack: () -> Unit,
) {
    val viewModel: GameDetailViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    GameDetailScreen(
        modifier = modifier,
        state = state,
        onFavoriteChange = { viewModel.obtainEvent(GameDetailUiEvent.OnFavoriteChange(it)) },
        onRefresh = { viewModel.obtainEvent(GameDetailUiEvent.OnRefresh) },
        onNavigateBack = navigateBack,
        navigateToScreenshotScreen = navigateToScreenshotScreen,
        onGameStoreSelected = { viewModel.obtainEvent(GameDetailUiEvent.OnGameStoreSelected(it)) }
    )
}

@Composable
internal fun GameDetailScreen(
    modifier: Modifier = Modifier,
    state: GameDetailUiState,
    onFavoriteChange: (isFavorite: Boolean) -> Unit,
    onRefresh: () -> Unit,
    onNavigateBack: () -> Unit,
    navigateToScreenshotScreen: (selectedItem: Int, screenshots: List<ScreenshotEntity>) -> Unit,
    onGameStoreSelected: (storeId: Int) -> Unit,
) {
    when (state) {
        is GameDetailUiState.Detail -> {
            GameDetailView(
                modifier = modifier,
                detail = state.gameDetail.toGameDetailUiModel(),
                screenshotsState = state.screenshots,
                isFavorite = state.isFavorite,
                onFavoriteChange = onFavoriteChange,
                onBackClicked = onNavigateBack,
                onScreenshotClicked = { selectedItem ->
                    if (state.screenshots is ScreenshotsUiState.Success) {
                        navigateToScreenshotScreen(selectedItem, state.gameDetail.screenshots)
                    }
                },
                onGameStoreSelected = onGameStoreSelected
            )
        }

        is GameDetailUiState.Error -> {
            DefaultErrorView(
                modifier = modifier,
                message = state.errorMessage,
                onRefreshPressed = onRefresh
            )
        }

        GameDetailUiState.Loading -> {
            LoadingView(modifier = modifier)
        }
    }
}