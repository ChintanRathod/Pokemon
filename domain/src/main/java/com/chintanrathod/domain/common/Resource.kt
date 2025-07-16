package com.chintanrathod.domain.common

/**
 * A generic sealed class representing the state of a resource.
 *
 * This is commonly used for wrapping data in ViewModels to represent different UI states.
 *
 * @param T The type of data being wrapped.
 * @property data The actual data returned from a successful operation, or null.
 * @property error An optional error object in case of failure.
 */
sealed class Resource<T>(
    val data: T? = null,
    val error: AppError? = null,
) {
    /**
     * Represents a successful state with [data].
     */
    class Success<T>(data: T) : Resource<T>(data)

    /**
     * Represents a loading state. Optionally carries cached or partial [data].
     */
    class Loading<T>(data: T? = null) : Resource<T>(data)

    /**
     * Represents an error state with an [AppError] and optional [data].
     */
    class Error<T>(error: AppError, data: T? = null) : Resource<T>(data, error)
}

/**
 * Represents different types of application-level errors.
 */
sealed interface AppError {
    /**
     * Indicates there is no active internet connection.
     */
    object NoInternet : AppError

    /**
     * Represents an HTTP or network error.
     *
     * @property code The HTTP status code.
     * @property message Optional detailed message.
     */
    data class NetworkError(val code: Int, val message: String? = null) : AppError
}