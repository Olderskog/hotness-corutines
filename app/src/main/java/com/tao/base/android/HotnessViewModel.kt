package com.tao.base.android

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.tao.base.android.utils.AndroidUi
import com.tao.base.domain.GameRepository
import com.tao.base.domain.entities.GameOverview
import com.tao.base.domain.utils.Either
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.actor


class HotnessViewModel(private val gameRepo: GameRepository) : ViewModel() {

    private val mutableHotness = MutableLiveData<List<GameOverview>>().apply { value = emptyList() }
    private val mutableLoading = MutableLiveData<Boolean>().apply { value = false }
    private val mutableErrorMessage = MutableLiveData<String>().apply { value = "" }

    val hotness: LiveData<List<GameOverview>> = mutableHotness
    val loading: LiveData<Boolean> = mutableLoading
    val errorMessage: LiveData<String> = mutableErrorMessage

    private val actor = actor<UiAction>(AndroidUi, Channel.CONFLATED) {
        for (action in this) {
            when (action) {
                FetchHotness -> fetchHotness()
                is FetchGameDetails -> fetchGameDetails(action.gameId)
            }
        }
    }

    fun action(action: UiAction) = actor.offer(action)

    private suspend fun fetchHotness() {
        mutableLoading.value = true
        val maybeHotness = gameRepo.getHotness()
        mutableLoading.value = false

        when (maybeHotness) {
            is Either.Right -> {
                mutableErrorMessage.value = ""
                mutableHotness.value = maybeHotness.value
            }
            is Either.Left -> {
                mutableErrorMessage.value = maybeHotness.value.localizedMessage ?: "Unknown Error"
            }
        }
    }

    private suspend fun fetchGameDetails(gameId: Long) {
        mutableLoading.value = true
        val maybeGame = gameRepo.getGameDetails(gameId)
        mutableLoading.value = false

        when (maybeGame) {
            is Either.Right -> Log.i("HotnessViewModel", "GameDetails: ${maybeGame.value}")
            is Either.Left -> mutableErrorMessage.value = maybeGame.value.localizedMessage ?: "Unknown Error"
        }
    }

}

sealed class UiAction
object FetchHotness : UiAction()
data class FetchGameDetails(val gameId: Long) : UiAction()