package com.tao.datasource.utils

import android.util.Log
import kotlinx.coroutines.experimental.delay
import ru.gildor.coroutines.retrofit.Result


/**
 * Helper function for retrying network calls
 */
suspend fun <T: Any> retryNetwork(times: Int = 3,
                                  initialDelay: Long = 500,
                                  maxDelay: Long = 5000,
                                  factor: Double = 2.0,
                                  block: suspend () -> Result<T>) : Result<T> {
    var currentDelay = initialDelay
    repeat(times - 1) {
        val result = block()
        when(result) {
            is Result.Ok        -> return result
            is Result.Error     -> { Log.e("RetryNetwork", "Error: ${result.exception.localizedMessage}")}
            is Result.Exception -> { Log.e("RetryNetwork", "Exception: ${result.exception.localizedMessage}") }
        }

        delay(currentDelay)
        currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
    }

    return block()
}