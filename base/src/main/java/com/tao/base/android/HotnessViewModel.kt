package com.tao.base.android

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.tao.base.data.toDomain
import com.tao.base.domain.GameRepository
import com.tao.base.domain.entities.GameOverview
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.actor
import ru.gildor.coroutines.retrofit.Result
import javax.inject.Inject


class HotnessViewModel(private val gameRepo: GameRepository) : ViewModel() {

    private val mutableHotness = MutableLiveData<List<GameOverview>>().apply { value = emptyList() }
    private val mutableLoading = MutableLiveData<Boolean>().apply { value = false }
    private val mutableErrorMessage = MutableLiveData<String>().apply { value = "" }

    val hotness: LiveData<List<GameOverview>> = mutableHotness
    val loading: LiveData<Boolean> = mutableLoading
    val errorMessage: LiveData<String> = mutableErrorMessage

    private val actor = actor<UiAction>(Android, Channel.CONFLATED) {
        for (action in this) {
            when (action) {
                FetchHotness -> { fetchHotness() }
                is FetchGameDetails -> { fetchGameDetails(action.gameId) }
            }
        }
    }

    fun action(action: UiAction) = actor.offer(action)

    private suspend fun fetchHotness() {
        mutableLoading.value = true
        val result = gameRepo.getHotness()
        mutableLoading.value = false

        when (result) {
            is Result.Ok -> {
                mutableErrorMessage.value = ""
                mutableHotness.value = result.value.toDomain()
            }
            is Result.Error -> mutableErrorMessage.value = result.exception.localizedMessage
            is Result.Exception -> mutableErrorMessage.value = result.exception.localizedMessage
        }
    }

    private suspend fun fetchGameDetails(gameId: Long) {
        mutableLoading.value = true
        val result = gameRepo.getGameDetails(gameId)
        mutableLoading.value = false

        when (result) {
            is Result.Ok -> Log.i("HotnessViewModel", "GameDetails: ${result.value.toDomain()}")
            is Result.Error -> mutableErrorMessage.value = result.exception.localizedMessage
            is Result.Exception -> mutableErrorMessage.value = result.exception.localizedMessage
        }
    }

}

sealed class UiAction
object FetchHotness : UiAction()
data class FetchGameDetails(val gameId: Long) : UiAction()