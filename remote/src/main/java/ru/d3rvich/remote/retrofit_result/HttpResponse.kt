package ru.d3rvich.remote.retrofit_result

/**
 * Created by Ilya Deryabin at 09.02.2024
 */
interface HttpResponse {

    val statusCode: Int

    val statusMessage: String?

    val url: String?
}