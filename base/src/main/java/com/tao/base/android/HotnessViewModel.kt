package com.tao.base.android

import com.tao.base.domain.GameRepository
import javax.inject.Inject


class HotnessViewModel
    @Inject constructor(private val gameRepo: GameRepository) {

    suspend fun fetchHotness() = gameRepo.getHotness()

    suspend fun fetchGameDetails(gameId: Long) = gameRepo.getGameDetails(gameId)

}
