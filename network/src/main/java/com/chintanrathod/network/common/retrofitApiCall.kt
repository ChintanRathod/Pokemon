package com.chintanrathod.network.common

import com.chintanrathod.data.common.NetworkResponse
import retrofit2.Response
import java.net.ConnectException
import java.net.UnknownHostException

/**
 * Executes a Retrofit API call and maps the [Response] to a [NetworkResponse].
 *
 * This function makes the network call via [call], attempts to parse the successful response
 * body using the provided [parse] function, and returns a [NetworkResponse] indicating the
 * outcome (Success, ClientError, ServerError, or NoInternet).
 *
 * @param call suspend function that executes the Retrofit API call returning [Response]<In>
 * @param parse suspend function to convert the response body from type [In] to [Out]
 * @return a [NetworkResponse] wrapping the parsed result or an error state
 */
suspend fun <Out, In> retrofitApiCall(
    call: suspend () -> Response<In>,
    parse: suspend (In) -> Out,
): NetworkResponse<Out> {

    var code = -1 // Parsing error

    return try {
        val response = call()
        val body = response.body()
        code = response.code()
        when {
            // Check for success with body not null then success with parsing
            response.isSuccessful && body != null -> NetworkResponse.Success(parse(body))
            // Check for success with body null then error with message
            response.isSuccessful && body == null -> mapNetworkError(code)
            else -> {
                // check for code and message
                mapNetworkError(code, response.errorBody().toString())
            }
        }
    } catch (e: ConnectException) {
        NetworkResponse.NoInternet()
    } catch (e: UnknownHostException) {
        NetworkResponse.NoInternet()
    } catch (e: Throwable) {
        mapNetworkError(code = code, e.message)
    }
}

private fun <T> mapNetworkError(code: Int, message: String? = null) =
    when (code) {
        404 -> NetworkResponse.NoInternet()
        in 400..499 -> NetworkResponse.ClientError<T>(code = code, message)
        in 500..599 -> NetworkResponse.ServerError<T>(code = code, message)
        else -> NetworkResponse.ClientError<T>(code = code, message)
    }