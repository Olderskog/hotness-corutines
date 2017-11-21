package com.tao.thehotness_corutines.data

import com.tao.thehotness_corutines.data.entities.TGame
import com.tao.thehotness_corutines.data.entities.TGameOverview
import com.tao.thehotness_corutines.domain.entities.Game
import com.tao.thehotness_corutines.domain.entities.GameOverview


fun TGameOverview.toDomain() = GameOverview(gameId, rank, name, thumbnail ?: "", yearPublished ?: "")

fun List<TGameOverview>?.toDomain() : List<GameOverview> {
    if (this == null)
        return emptyList()

    return map { it.toDomain() }
}

fun TGame.toDomain() = Game(gameId,
                            rank,
                            name,
                   thumbnail ?: "",
                yearPublished ?: "",
        description ?: "",
        image ?: "",
        minPlayers ?: 1,
        maxPlayers ?: 1,
        playingTime ?: 0,
        mechanics ?: emptyList(),
        isExpansion ?: false,
        bggRating ?: 0.0,
        averageRating ?: 0.0)