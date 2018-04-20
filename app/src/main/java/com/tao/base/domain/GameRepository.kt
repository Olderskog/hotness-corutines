package com.tao.base.domain

import com.tao.base.data.RemoteGameSource
import com.tao.base.domain.entities.Expansion
import javax.inject.Inject


class GameRepository
    @Inject constructor(private val gameSource: RemoteGameSource) {

    suspend fun getHotness() = gameSource.fetchHotness()

    suspend fun getGameDetails(gameId: Long) = gameSource.fetchGameDetails(gameId)

    suspend fun getExpansions(expansions: List<Expansion>) = gameSource.fetchExpansions(expansions)

}