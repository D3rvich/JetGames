package ru.d3rvich.remote.retrofit_result

/**
 * Created by Ilya Deryabin at 09.02.2024
 */
class HttpException(
    val statusCode: Int,
    val statusMessage: String? = null,
    val url: String? = null,
    cause: Throwable? = null,
) : Exception(null, cause)