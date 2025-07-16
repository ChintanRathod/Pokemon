package com.chintanrathod.data.common

import com.chintanrathod.domain.common.AppError
import com.chintanrathod.domain.common.Resource

/**
 * A helper function to convert a [NetworkResponse] into a [Resource].
 *
 * This abstracts away network result handling and transforms various network outcomes
 * (success, client/server error, no internet) into app-specific [Resource] states.
 *
 * @param Result The expected type of the successful network response.
 * @param network A suspend function that returns a [NetworkResponse] of type [Result].
 *
 * @return A [Resource] representing success or an appropriate [AppError].
 */
internal suspend inline fun <Result> networkBoundResource(
    crossinline network: suspend () -> NetworkResponse<Result>,
) = when (val response = network()) {
    is NetworkResponse.ClientError -> Resource.Error(
        AppError.NetworkError(response.code,
        response.message))
    is NetworkResponse.ServerError -> Resource.Error(AppError.NetworkError(response.code,
        response.message))
    is NetworkResponse.NoInternet -> Resource.Error(AppError.NoInternet)
    is NetworkResponse.Success -> Resource.Success(response.response)
}