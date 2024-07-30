package ru.d3rvich.remote.util

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by Ilya Deryabin at 05.02.2024
 */
internal class AuthInterceptor(private val apiKey: String) : Interceptor {
    private companion object {
        const val PARAMETER_NAME = "key"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val newUrl = chain.request()
            .url
            .newBuilder()
            .addQueryParameter(PARAMETER_NAME, apiKey)
            .build()
        val request = chain.request().newBuilder().url(newUrl).build()
        return chain.proceed(request)
    }
}