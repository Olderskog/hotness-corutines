package com.tao.base.android

import android.os.Handler
import android.os.Looper
import android.util.Log
import kotlinx.coroutines.experimental.delay
import ru.gildor.coroutines.retrofit.Result
import kotlin.coroutines.experimental.AbstractCoroutineContextElement
import kotlin.coroutines.experimental.Continuation
import kotlin.coroutines.experimental.ContinuationInterceptor


private class AndroidContinuation<in T>(val cont: Continuation<T>) : Continuation<T> by cont {
    override fun resume(value: T) {
        if (Looper.myLooper() == Looper.getMainLooper()) cont.resume(value)
        else Handler(Looper.getMainLooper()).post { cont.resume(value) }
    }
    override fun resumeWithException(exception: Throwable) {
        if (Looper.myLooper() == Looper.getMainLooper()) cont.resumeWithException(exception)
        else Handler(Looper.getMainLooper()).post { cont.resumeWithException(exception) }
    }
}

object Android : AbstractCoroutineContextElement(ContinuationInterceptor), ContinuationInterceptor {
    override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T> =
            AndroidContinuation(continuation)
}

suspend fun <T: Any> retryNetwork(times: Int = 3,
                                  initialDelay: Long = 500,
                                  maxDelay: Long = 5000,
                                  factor: Double = 2.0,
                                  block: suspend () -> Result<T>) : Result<T> {
    var currentDelay = initialDelay
    repeat(times - 1) {
        val result = block()
        when(result) {
            is Result.Ok -> return result
            is Result.Error -> { Log.e("RetryNetwork", "Error: ${result.exception.localizedMessage}")}
            is Result.Exception -> { Log.e("RetryNetwork", "Exception: ${result.exception.localizedMessage}") }
        }

        delay(currentDelay)
        currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
    }

    return block()
}