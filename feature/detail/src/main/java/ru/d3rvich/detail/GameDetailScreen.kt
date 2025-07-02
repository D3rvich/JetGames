package ru.d3rvich.detail

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.d3rvich.common.components.DefaultErrorView
import ru.d3rvich.core.domain.entities.ScreenshotEntity
import ru.d3rvich.detail.model.GameDetailUiAction
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
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { contentPadding ->
        GameDetailScreen(
            modifier = Modifier.padding(contentPadding),
            state = state,
            onFavoriteChange = { viewModel.obtainEvent(GameDetailUiEvent.OnFavoriteChange(it)) },
            onRefresh = { viewModel.obtainEvent(GameDetailUiEvent.OnRefresh) },
            onNavigateBack = navigateBack,
            navigateToScreenshotScreen = navigateToScreenshotScreen,
            onGameStoreSelected = { viewModel.obtainEvent(GameDetailUiEvent.OnGameStoreSelected(it)) }
        )
    }
    LaunchedEffect(viewModel) {
        viewModel.uiAction.collect { uiAction ->
            when (uiAction) {
                GameDetailUiAction.ShowGameStoreDownloadError -> {
                    snackbarHostState.showSnackbar(message = "Check internet connection.")
                }
            }
        }
    }
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