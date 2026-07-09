package ru.d3rvich.feature.detail

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.d3rvich.common.components.DefaultErrorView
import ru.d3rvich.core.entity.ScreenshotEntity
import ru.d3rvich.feature.detail.model.ScreenshotsState
import ru.d3rvich.feature.detail.store.GameDetailStore
import ru.d3rvich.feature.detail.views.GameDetailView
import ru.d3rvich.feature.detail.views.LoadingView

/**
 * Created by Ilya Deryabin at 24.02.2024
 */
@Composable
fun GameDetailScreen(
    gameId: Int,
    navigateToScreenshotScreen: (selectedItem: Int, screenshots: List<ScreenshotEntity>) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GameDetailViewModel = koinViewModel(key = gameId.toString()) { parametersOf(gameId) },
) {
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
            onIntent = { viewModel.obtainEvent(it) },
            onNavigateBack = navigateBack,
            navigateToScreenshotScreen = navigateToScreenshotScreen,
        )
    }
}

@Composable
internal fun GameDetailScreen(
    state: GameDetailStore.State,
    onIntent: (GameDetailStore.Intent) -> Unit,
    onNavigateBack: () -> Unit,
    navigateToScreenshotScreen: (selectedItem: Int, screenshots: List<ScreenshotEntity>) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state) {
        is GameDetailStore.State.GameDetail -> {
            GameDetailView(
                modifier = modifier,
                detail = state.gameDetail,
                screenshotsState = state.screenshots,
                storeUiState = state.stores,
                onFavoriteChange = { onIntent(GameDetailStore.Intent.OnFavoriteChange(it)) },
                onBackClicked = onNavigateBack,
                onScreenshotClicked = { selectedItem ->
                    if (state.screenshots is ScreenshotsState.Success) {
                        navigateToScreenshotScreen(selectedItem, state.gameDetail.screenshots)
                    }
                },
                onGameStoreSelected = { onIntent(GameDetailStore.Intent.OnGameStoreSelected(it)) }
            )
        }

        is GameDetailStore.State.Error -> {
            DefaultErrorView(
                modifier = modifier,
                message = state.errorMessage,
                onRefreshPressed = { onIntent(GameDetailStore.Intent.OnRefresh) }
            )
        }

        GameDetailStore.State.Loading -> {
            LoadingView(modifier = modifier)
        }
    }
}