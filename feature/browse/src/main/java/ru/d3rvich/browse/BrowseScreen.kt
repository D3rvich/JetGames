package ru.d3rvich.browse

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.d3rvich.browse.model.BrowseUiState
import ru.d3rvich.browse.views.GenresView
import ru.d3rvich.browse.views.PlatformsView

/**
 * Created by Ilya Deryabin at 05.06.2024
 */
@Composable
fun BrowseScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
) {
    val viewModel: BrowseViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    BrowseScreen(
        state = state,
        modifier = modifier,
        contentPadding = contentPadding,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BrowseScreen(
    modifier: Modifier = Modifier,
    state: BrowseUiState,
    contentPadding: PaddingValues,
) {
    Scaffold(modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(title = { Text(text = stringResource(R.string.browse)) })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(contentPadding),
        ) {
            GenresView(genresStatus = state.genres)
            PlatformsView(platformsStatus = state.platforms)
        }
    }
}