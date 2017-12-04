package com.tao.base.data

import com.tao.base.android.retryNetwork
import ru.gildor.coroutines.retrofit.awaitResult
import javax.inject.Inject


class RemoteGameSource
    @Inject constructor(private val service: BGGService) {

    suspend fun fetchHotness() = retryNetwork { service.getHotness().awaitResult() }

    suspend fun fetchGameDetails(gameId: Long) = retryNetwork { service.getDetails(gameId).awaitResult() }
}