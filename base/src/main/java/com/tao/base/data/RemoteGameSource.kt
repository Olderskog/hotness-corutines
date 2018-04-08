package com.tao.base.data

import com.tao.base.android.utils.retryNetwork
import com.tao.base.data.utils.retryNetwork
import com.tao.base.domain.entities.Game
import com.tao.base.domain.entities.GameOverview
import com.tao.base.domain.utils.Either
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
}