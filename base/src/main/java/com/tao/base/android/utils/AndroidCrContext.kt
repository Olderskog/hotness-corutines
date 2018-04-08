package com.tao.base.android.utils

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

object AndroidUi : AbstractCoroutineContextElement(ContinuationInterceptor), ContinuationInterceptor {
    override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T> =
            AndroidContinuation(continuation)
}
