package com.tao.thehotness_corutines.data

import com.tao.thehotness_corutines.domain.GameOverview


fun TGameOverview.toDomain() = GameOverview(gameId, rank, name, thumbnail, yearPublished)
fun List<TGameOverview>.toDomain() = map { it.toDomain() }