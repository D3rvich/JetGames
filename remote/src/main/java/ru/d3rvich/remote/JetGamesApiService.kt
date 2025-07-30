package ru.d3rvich.remote

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.d3rvich.remote.model.ApiPagingResult
import ru.d3rvich.remote.model.StoreLink
import ru.d3rvich.remote.model.GenreFull
import ru.d3rvich.remote.model.Platform
import ru.d3rvich.remote.model.Screenshot
import ru.d3rvich.remote.model.game.Game
import ru.d3rvich.remote.model.game.GameDetail
import ru.d3rvich.remote.result_adapter.ResultAdapterFactory
import ru.d3rvich.remote.retrofit_result.NetworkResult
import ru.d3rvich.remote.util.AuthInterceptor

/**
 * Created by Ilya Deryabin at 05.02.2024
 *
 * Api documentation [here](https://api.rawg.io/docs/)
 */
@Deprecated("")
interface JetGamesApiService {

    companion object {
        internal const val BASE_URL = "https://api.rawg.io/api/"
    }

    /**
     * Api [doc](https://api.rawg.io/docs/#tag/games)
     * */
    @GET("games")
    suspend fun getGames(
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int,
        @Query("search") search: String? = null,
        @Query("search_precise") searchPrecise: Boolean = true,
        @Query("ordering") sorting: String? = null,
        @Query("tags") tags: String? = null,
        @Query("platforms") platforms: String? = null,
        @Query("genres") genres: String? = null,
        @Query("metacritic") metacritic: String? = null,
    ): NetworkResult<ApiPagingResult<Game>>

    /**
     * Api [doc](https://api.rawg.io/docs/#operation/games_read)
     * */
    @GET("games/{id}")
    suspend fun getGameDetail(@Path("id") gameId: Int): NetworkResult<GameDetail>

    /**
     * Api [doc](https://api.rawg.io/docs/#operation/games_screenshots_list)
     * */
    @GET("games/{id}/screenshots")
    suspend fun getScreenshots(
        @Path("id") gameId: Int,
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 10,
    ): NetworkResult<ApiPagingResult<Screenshot>>

    /**
     * Api [doc](https://api.rawg.io/docs/#operation/games_stores_list)
     * */
    @GET("games/{id}/stores")
    suspend fun getGameStoresById(
        @Path("id") gameId: Int,
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = RAWG_MAX_PAGE_SIZE
    ): NetworkResult<ApiPagingResult<StoreLink>>

    /**
     * Api [doc](https://api.rawg.io/docs/#operation/platforms_list)
     * */
    @GET("platforms")
    suspend fun getPlatforms(
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int,
    ): NetworkResult<ApiPagingResult<Platform>>

    /**
     * Api [doc](https://api.rawg.io/docs/#operation/genres_list)
     * */
    @GET("genres")
    suspend fun getGenres(
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int,
    ): NetworkResult<ApiPagingResult<GenreFull>>
}

fun JetGamesApiService(apiKey: String): JetGamesApiService {
    val json = Json { ignoreUnknownKeys = true }
    val retrofit = retrofit(
        baseUrl = JetGamesApiService.BASE_URL,
        okHttpClient = okHttpClient(apiKey),
        json = json
    )
    return retrofit.create()
}

private fun retrofit(
    baseUrl: String,
    okHttpClient: OkHttpClient,
    json: Json,
): Retrofit {
    return Retrofit.Builder().apply {
        baseUrl(baseUrl)
        client(okHttpClient)
        addCallAdapterFactory(ResultAdapterFactory())
        addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    }.build()
}

private fun okHttpClient(apiKey: String): OkHttpClient {
    val loggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    val authInterceptor = AuthInterceptor(apiKey)
    return OkHttpClient.Builder().apply {
        addInterceptor(loggingInterceptor)
        addInterceptor(authInterceptor)
    }.build()
}

interface JetGamesNetworkDataSource {
    suspend fun getGames(
        page: Int,
        pageSize: Int,
        search: String? = null,
        searchPrecise: Boolean = true,
        sorting: String? = null,
        tags: String? = null,
        platforms: String? = null,
        genres: String? = null,
        metacritic: String? = null,
    ): NetworkResult<ApiPagingResult<Game>>

    suspend fun getGameDetail(gameId: Int): NetworkResult<GameDetail>

    suspend fun getScreenshots(
        gameId: Int,
        page: Int = 1,
        pageSize: Int = 10,
    ): NetworkResult<ApiPagingResult<Screenshot>>

    suspend fun getGameStoresById(
        gameId: Int,
        page: Int = 1,
        pageSize: Int = RAWG_MAX_PAGE_SIZE
    ): NetworkResult<ApiPagingResult<StoreLink>>

    suspend fun getPlatforms(
        page: Int,
        pageSize: Int,
    ): NetworkResult<ApiPagingResult<Platform>>

    suspend fun getGenres(
        page: Int,
        pageSize: Int,
    ): NetworkResult<ApiPagingResult<GenreFull>>
}

private const val RAWG_MAX_PAGE_SIZE = 40