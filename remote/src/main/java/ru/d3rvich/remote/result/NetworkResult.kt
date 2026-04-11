package ru.d3rvich.remote.result

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.resources.get
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import kotlinx.serialization.SerializationException
import okio.IOException

sealed interface NetworkResult<out T> {
    class Success<T>(val value: T) : NetworkResult<T>
    sealed interface Failure<E : Throwable> : NetworkResult<Nothing> {
        val error: E?

        /**
         * 4хх и 5хх ответы с сервера
         */
        class ServerError(
            override val error: ResponseException?,
            val statusCode: Int,
            val statusMassage: String? = null,
            val url: String? = null,
        ) : Failure<ResponseException>

        /**
         * Проблемы с сетью или таймаут
         */
        class ConnectivityError(override val error: IOException?) : Failure<IOException>

        /**
         * Ошибка при парсинге JSON
         */
        class SerializationError(override val error: SerializationException?) :
            Failure<SerializationException>

        /**
         * Прочие ошибки
         */
        class Error(override val error: Exception?) : Failure<Exception>
    }
}

internal suspend inline fun <reified T : Any, reified R : Any> HttpClient.safeApiCall(resource: () -> T): NetworkResult<R> =
    try {
        NetworkResult.Success(get(resource()).body<R>())
    } catch (e: ResponseException) {
        NetworkResult.Failure.ServerError(
            error = e,
            statusCode = e.response.status.value,
            statusMassage = e.response.bodyAsText(),
            url = e.response.request.url.toString()
        )
    } catch (e: IOException) {
        NetworkResult.Failure.ConnectivityError(e)
    } catch (e: SerializationException) {
        NetworkResult.Failure.SerializationError(e)
    } catch (e: Exception) {
        NetworkResult.Failure.Error(e)
    }