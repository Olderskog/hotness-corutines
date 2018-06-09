package com.tao.base.hotness.domain

import com.tao.base.hotness.data.RemoteGameSource
import com.tao.base.hotness.domain.entities.Expansion
import javax.inject.Inject


class GameRepository
    @Inject constructor(private val gameSource: RemoteGameSource) {

    suspend fun getHotness() = gameSource.fetchHotness()

    suspend fun getGameDetails(gameId: Long) = gameSource.fetchGameDetails(gameId)

    suspend fun getExpansions(expansions: List<Expansion>) = gameSource.fetchExpansions(expansions)

}