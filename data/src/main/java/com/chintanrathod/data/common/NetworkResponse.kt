package com.chintanrathod.data.common

/**
 * Represents a generic sealed result of a network call.
 *
 * @param T The type of the expected successful response.
 */
sealed interface NetworkResponse<T>{

    /**
     * Indicates the request was successful and returned [response].
     */
    data class Success<T>(val response : T) : NetworkResponse<T>

    /**
     * Indicates there was no internet connection available for the request.
     *
     * @param message Optional message indicating connection error details.
     */
    data class NoInternet<T>(val message : String? = null) :
        NetworkResponse<T>

    /**
     * Indicates a client-side error (typically HTTP 4xx).
     *
     * @param code HTTP status code.
     * @param message Optional error message from the server.
     */
    data class ClientError<T>(val code : Int, val message : String? = null) :
        NetworkResponse<T>

    /**
     * Indicates a server-side error (typically HTTP 5xx).
     *
     * @param code HTTP status code.
     * @param message Optional error message from the server.
     */
    data class ServerError<T>(val code : Int, val message : String? = null) :
        NetworkResponse<T>
}