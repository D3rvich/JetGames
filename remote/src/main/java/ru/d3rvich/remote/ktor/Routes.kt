package ru.d3rvich.remote.ktor

import io.ktor.resources.Resource
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName

@OptIn(InternalSerializationApi::class)
internal object Routes {
    @Resource("games")
    class Games(
        @SerialName("page") val page: Int? = null,
        @SerialName("page_size") val pageSize: Int? = null,
        @SerialName("search") val search: String? = null,
        @SerialName("search_precise") val searchPrecise: Boolean? = null,
        @SerialName("ordering") val sorting: String? = null,
        @SerialName("tags") val tags: String? = null,
        @SerialName("platforms") val platforms: String? = null,
        @SerialName("genres") val genres: String? = null,
        @SerialName("metacritic") val metacritic: String? = null,
    ) {

        @Resource("{id}")
        class Detail(val parent: Games = Games(), @SerialName("id") val gameId: Int) {

            @Resource("screenshots")
            class Screenshots(
                @SerialName("id") val gameId: Int,
                @SerialName("page") val page: Int = 1,
                @SerialName("page_size") val pageSize: Int = 10,
            )

            @Resource("stores")
            class Stores(
                @SerialName("id") val gameId: Int,
                @SerialName("page") val page: Int = 1,
                @SerialName("page_size") val pageSize: Int = RAWG_MAX_PAGE_SIZE
            )
        }
    }

    @Resource("platforms")
    class Platforms(
        @SerialName("page") val page: Int,
        @SerialName("page_size") val pageSize: Int,
    )

    @Resource("genres")
    class Genres(
        @SerialName("page") val page: Int,
        @SerialName("page_size") val pageSize: Int,
    )
}

private const val RAWG_MAX_PAGE_SIZE = 40
