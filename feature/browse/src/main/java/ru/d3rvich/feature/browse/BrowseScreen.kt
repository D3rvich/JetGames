package ru.d3rvich.feature.browse

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import ru.d3rvich.core.domain.entities.GenreFullEntity
import ru.d3rvich.core.domain.entities.PlatformEntity
import ru.d3rvich.core.domain.model.LoadingResult
import ru.d3rvich.feature.browse.views.GenresView
import ru.d3rvich.feature.browse.views.PlatformsView

/**
 * Created by Ilya Deryabin at 05.06.2024
 */
@Composable
fun BrowseScreen(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    browseViewModel: BrowseViewModel = koinViewModel(),
) {
    val genres by browseViewModel.genres.collectAsStateWithLifecycle()
    val platforms by browseViewModel.platforms.collectAsStateWithLifecycle()
    BrowseScreen(
        contentPadding = contentPadding,
        genres = genres,
        platforms = platforms,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BrowseScreen(
    genres: LoadingResult<List<GenreFullEntity>>,
    platforms: LoadingResult<List<PlatformEntity>>,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
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
            GenresView(genresStatus = genres)
            PlatformsView(platformsStatus = platforms)
        }
    }
}