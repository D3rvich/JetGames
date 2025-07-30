package ru.d3rvich.remote.result_adapter

import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import ru.d3rvich.remote.retrofit_result.NetworkResult
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Created by Ilya Deryabin at 09.02.2024
 */
internal class ResultAdapterFactory : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit,
    ): CallAdapter<*, *>? {
        val rawReturnType: Class<*> = getRawType(returnType)
        if (rawReturnType == Call::class.java) {
            if (returnType is ParameterizedType) {
                val callInnerType: Type = getParameterUpperBound(0, returnType)
                if (getRawType(callInnerType) == NetworkResult::class.java) {
                    // resultType is Call<Result<*>> | callInnerType is Result<*>
                    if (callInnerType is ParameterizedType) {
                        val resultInnerType = getParameterUpperBound(0, callInnerType)
                        return ResultCallAdapter<Any?>(resultInnerType)
                    }
                    return ResultCallAdapter<Nothing>(Nothing::class.java)
                }
            }
        }

        return null
    }
}

private class ResultCallAdapter<R>(private val type: Type) :
    CallAdapter<R, Call<NetworkResult<R>>> {

    override fun responseType() = type

    override fun adapt(call: Call<R>): Call<NetworkResult<R>> = ResultCall(call)
}