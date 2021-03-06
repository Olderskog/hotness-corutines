package com.tao.base.hotness.data

import com.tao.base.hotness.domain.entities.Expansion
import com.tao.base.hotness.domain.entities.Game
import com.tao.base.hotness.domain.entities.GameOverview
import com.tao.base.hotness.domain.utils.Either
import com.tao.datasource.remote.BGGService
import com.tao.datasource.utils.retryNetwork
import ru.gildor.coroutines.retrofit.Result
import ru.gildor.coroutines.retrofit.awaitResult
import javax.inject.Inject


class RemoteGameSource
    @Inject constructor(private val service: BGGService) {

    /**
     * Fetch the hotness list from BoardGameGeeks
     */
    suspend fun fetchHotness(): Either<Throwable, List<GameOverview>> {
        val response = retryNetwork { service.getHotness().awaitResult() }

        return when (response) {
            is Result.Ok -> Either.Right(response.value.toDomain())
            is Result.Error -> Either.Left(response.exception)
            is Result.Exception -> Either.Left(response.exception)
        }
    }

    /**
     * Fetch details about a given game based on the provided Id
     */
    suspend fun fetchGameDetails(gameId: Long) : Either<Throwable, Game> {
        val response = retryNetwork { service.getDetails(gameId).awaitResult() }

        return when (response) {
            is Result.Ok -> Either.Right(response.value.toDomain())
            is Result.Error -> Either.Left(response.exception)
            is Result.Exception -> Either.Left(response.exception)
        }
    }

    suspend fun fetchExpansions(expansions: List<Expansion>) : List<Either<Throwable, Game>> /*Either<Throwable, List<Expansion>>*/ {
        val response = expansions.map { retryNetwork { service.getDetails(it.gameId).awaitResult() } }

        return response.map { result ->
            when (result) {
                is Result.Ok -> Either.Right(result.value.toDomain())
                is Result.Error -> Either.Left(result.exception)
                is Result.Exception -> Either.Left(result.exception)
            }
        }
    }
}