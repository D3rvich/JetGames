package ru.d3rvich.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import ru.d3rvich.core.domain.entities.GameEntity
import ru.d3rvich.core.domain.entities.SortingEntity
import ru.d3rvich.core.domain.entities.getReversed
import ru.d3rvich.core.domain.model.MetacriticRange
import ru.d3rvich.core.domain.model.Result
import ru.d3rvich.core.domain.preferences.FilterPreferencesBody
import ru.d3rvich.data.mapper.toGameEntity
import ru.d3rvich.data.util.safeApiCall
import ru.d3rvich.remote.JetGamesApiService
import kotlin.math.roundToInt

/**
 * Created by Ilya Deryabin at 12.02.2024
 */
internal class GamesPagingSource @AssistedInject constructor(
    private val apiService: JetGamesApiService,
    @Assisted private val search: String = "",
    @Assisted private val filterPreferencesBody: FilterPreferencesBody,
) : PagingSource<Int, GameEntity>() {
    override fun getRefreshKey(state: PagingState<Int, GameEntity>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(position)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GameEntity> {
        val pageNumber = params.key ?: INITIAL_PAGE_NUMBER
        val pageSize = params.loadSize.coerceAtMost(MAX_PAGE_SIZE)
        val sorting = filterPreferencesBody.let { body ->
            if (body.sortBy == SortingEntity.NoSorting) {
                null
            } else {
                if (body.isReversed) body.sortBy.getReversed()
                else body.sortBy.toString()
            }
        }?.lowercase()
        val platforms = if (filterPreferencesBody.selectedPlatforms.isNotEmpty()) {
            filterPreferencesBody.selectedPlatforms.map { it.id }.joinToString(",")
        } else {
            null
        }
        val genres = if (filterPreferencesBody.selectedGenres.isNotEmpty()) {
            filterPreferencesBody.selectedGenres.map { it.id }.joinToString(",")
        } else {
            null
        }
        val metacritic = if (filterPreferencesBody.metacriticRange != MetacriticRange.None) {
            with(filterPreferencesBody.metacriticRange) {
                "${min.roundToInt()},${max.roundToInt()}"
            }
        } else {
            null
        }
        return when (val result = safeApiCall {
            apiService.getGames(
                page = pageNumber,
                pageSize = pageSize,
                search = search.ifBlank { null },
                sorting = sorting,
                platforms = platforms,
                genres = genres,
                metacritic = metacritic
            )
        }) {
            is Result.Success -> {
                val games = result.value.results.map { it.toGameEntity() }
                val prevPageNumber = if (result.value.previous != null) pageNumber - 1 else null
                val nextPageNumber = if (result.value.next != null) pageNumber + 1 else null
                LoadResult.Page(games, prevPageNumber, nextPageNumber)
            }

            is Result.Failure -> {
                LoadResult.Error(result.throwable)
            }
        }
    }

    private companion object {
        const val INITIAL_PAGE_NUMBER = 1
        const val MAX_PAGE_SIZE = 20
    }

    @AssistedFactory
    internal interface GamesPagingSourceFactory {
        fun create(
            search: String = "",
            filterPreferencesBody: FilterPreferencesBody,
        ): GamesPagingSource
    }
}