package ru.d3rvich.remote.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.resources.Resources
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import ru.d3rvich.network.BuildConfig
import ru.d3rvich.remote.JetGamesNetworkDataSource
import ru.d3rvich.remote.ktor.JetGamesNetworkClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Singleton
    @Provides
    fun provideHttpClient(): HttpClient = HttpClient(Android) {
        install(Logging) {
            logger = Logger.ANDROID
            level = LogLevel.BODY
        }
        install(Resources)
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
                host = "api.rawg.io/api"
                parameters.append("key", BuildConfig.API_KEY)
            }
            contentType(ContentType.Application.Json)
        }
    }

    @OptIn(InternalSerializationApi::class)
    @Singleton
    @Provides
    fun provideNetworkDataSource(client: HttpClient): JetGamesNetworkDataSource =
        JetGamesNetworkClient(client = client)
}