package com.example.bitcoininfoapp.data.models

/**
 * Wrapper class that describes the state of response at any point
 */
data class ResultResponse<T>(val responseStatus: Status,
                             val data: T? = null,
                             val errorData: Throwable? = null)


/**
 * ResponseStatus is a good common example for handling responses like: the network response with status type ect,
 */
enum class Status {
    LOADING,
    SUCCESS,
    FAILURE
}