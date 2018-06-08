package com.tao.base.data

import com.tao.base.domain.entities.Expansion
import com.tao.base.domain.entities.Game
import com.tao.base.domain.entities.GameOverview
import com.tao.datasource.remote.entities.TExpansion
import com.tao.datasource.remote.entities.TGame
import com.tao.datasource.remote.entities.TGameOverview


fun TExpansion.toDomain() = Expansion(gameId, name)

fun List<TExpansion>.toDomainExpansion() = this.map { it.toDomain() }

fun TGameOverview.toDomain() = GameOverview(gameId,
                                            rank ?: -1,
                                                                               name,
                                            thumbnail ?: "",
                                            yearPublished ?: "")

fun List<TGameOverview>?.toDomain() : List<GameOverview> {
    if (this == null)
        return emptyList()

    return map { it.toDomain() }
}

fun TGame.toDomain() = Game(gameId,
                            rank ?: -1,
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
                                                               averageRating ?: 0.0,
                                                               expansions?.toDomainExpansion() ?: emptyList())