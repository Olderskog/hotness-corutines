package com.tao.thehotness_corutines.android

import com.tao.thehotness_corutines.data.BGGService
import ru.gildor.coroutines.retrofit.awaitResult
import javax.inject.Inject


class HotnessViewModel
    @Inject constructor(private val service: BGGService) {

    suspend fun fetchHotness() = retryNetwork { service.getHotness().awaitResult() }

    suspend fun fetchGameDetails(gameId: Long) = retryNetwork { service.getDetails(gameId).awaitResult() }

}
