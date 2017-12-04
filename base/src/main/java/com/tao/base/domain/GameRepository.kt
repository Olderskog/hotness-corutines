package com.tao.base.domain

import com.tao.base.data.RemoteGameSource
import javax.inject.Inject


class GameRepository
    @Inject constructor(private val gameSource: RemoteGameSource) {

    suspend fun getHotness() = gameSource.fetchHotness()

    suspend fun getGameDetails(gameId: Long) = gameSource.fetchGameDetails(gameId)

}