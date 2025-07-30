package ru.d3rvich.remote.result_adapter

import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.d3rvich.remote.retrofit_result.HttpException
import ru.d3rvich.remote.retrofit_result.NetworkResult
import java.io.IOException

/**
 * Created by Ilya Deryabin at 09.02.2024
 */
@Suppress("UNCHECKED_CAST")
internal class ResultCall<T>(proxy: Call<T>) : CallDelegate<T, NetworkResult<T>>(proxy) {

    override fun enqueueImpl(callback: Callback<NetworkResult<T>>) {
        proxy.enqueue(ResultCallback(this, callback))
    }

    override fun cloneImpl(): ResultCall<T> {
        return ResultCall(proxy.clone())
    }

    private class ResultCallback<T>(
        private val proxy: ResultCall<T>,
        private val callback: Callback<NetworkResult<T>>,
    ) : Callback<T> {

        override fun onResponse(call: Call<T>, response: Response<T>) {
            val result: NetworkResult<T>
            if (response.isSuccessful) {
                result = NetworkResult.Success.HttpResponse(
                    value = response.body() as T,
                    statusCode = response.code(),
                    statusMessage = response.message(),
                    url = call.request().url.toString(),
                )
            } else {
                result = NetworkResult.Failure.HttpError(
                    HttpException(
                        statusCode = response.code(),
                        statusMessage = response.message(),
                        url = call.request().url.toString(),
                    )
                )
            }
            callback.onResponse(proxy, Response.success(result))
        }

        override fun onFailure(call: Call<T>, error: Throwable) {
            val result = when (error) {
                is retrofit2.HttpException -> NetworkResult.Failure.HttpError(
                    HttpException(error.code(), error.message(), cause = error)
                )

                is IOException -> NetworkResult.Failure.Error(error)
                else -> NetworkResult.Failure.Error(error)
            }

            callback.onResponse(proxy, Response.success(result))
        }
    }

    override fun timeout(): Timeout {
        return Timeout.NONE
    }
}