package ru.d3rvich.feature.browse.ui

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
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kotlinx.collections.immutable.ImmutableList
import ru.d3rvich.core.entity.GenreFullEntity
import ru.d3rvich.core.entity.PlatformEntity
import ru.d3rvich.core.model.Result
import ru.d3rvich.feature.browse.api.BrowseComponent
import ru.d3rvich.feature.browse.ui.views.GenresView
import ru.d3rvich.feature.browse.ui.views.PlatformsView

/**
 * Created by Ilya Deryabin at 05.06.2024
 */
@Composable
fun BrowseContent(
    component: BrowseComponent,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    val genres by component.genres.subscribeAsState()
    val platforms by component.platforms.subscribeAsState()
    BrowseContent(
        contentPadding = contentPadding,
        genres = genres,
        platforms = platforms,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BrowseContent(
    genres: Result<ImmutableList<GenreFullEntity>>,
    platforms: Result<ImmutableList<PlatformEntity>>,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
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
            GenresView(genresResult = genres)
            PlatformsView(result = platforms)
        }
    }
}