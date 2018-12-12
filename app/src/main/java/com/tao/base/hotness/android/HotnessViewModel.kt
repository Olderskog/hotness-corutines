package com.tao.base.hotness.android

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Log
import com.tao.base.hotness.domain.GameRepository
import com.tao.base.hotness.domain.entities.Game
import com.tao.base.hotness.domain.entities.GameOverview
import com.tao.base.hotness.domain.utils.Either
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.actor
import kotlin.coroutines.CoroutineContext


open class BaseViewModel : CoroutineScope, ViewModel() {

    private val parentJob = Job()
    override val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}

class HotnessViewModel(private val gameRepo: GameRepository) : BaseViewModel() {

    private val mutableHotness = MutableLiveData<List<GameOverview>>().apply { value = emptyList() }
    private val mutableGame = MutableLiveData<Game?>().apply { value = null }
    private val mutableExpansions = MutableLiveData<Game?>().apply { value = null }
    private val mutableLoading = MutableLiveData<Boolean>().apply { value = false }
    private val mutableLoadingExpansions = MutableLiveData<Boolean>().apply { value = false }
    private val mutableErrorMessage = MutableLiveData<String>().apply { value = "" }

    val hotness: LiveData<List<GameOverview>> = mutableHotness
    val game: LiveData<Game?> = mutableGame
    val expansions: LiveData<Game?> = mutableExpansions
    val loading: LiveData<Boolean> = mutableLoading
    val errorMessage: LiveData<String> = mutableErrorMessage

    private val perform = actor<UiAction>(Dispatchers.Main, Channel.UNLIMITED) {
        for (action in this) {
            Log.i("HotnessViewModel", "Performing action: $action")
            when (action) {
                FetchHotness -> fetchHotness()
                is FetchGameDetails -> fetchGameDetails(action.gameId)
                is FetchExpansion -> fetchExpansionDetails(action.gameId)
            }
        }
    }

    fun action(action: UiAction) = perform.offer(action)

    private suspend fun fetchHotness() {
        mutableLoading.value = true
        val maybeHotness = withContext(Dispatchers.IO) { gameRepo.getHotness() }
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
        val maybeGame = withContext(Dispatchers.IO){ gameRepo.getGameDetails(gameId) }
        mutableLoading.value = false

        when (maybeGame) {
            is Either.Right -> mutableGame.value = maybeGame.value
            is Either.Left -> mutableErrorMessage.value = maybeGame.value.localizedMessage ?: "Unknown Error"
        }
    }

    private suspend fun fetchExpansionDetails(gameId: Long) {
        mutableLoadingExpansions.value = true
        val maybeGame = withContext(Dispatchers.IO) { gameRepo.getGameDetails(gameId) }
        mutableLoadingExpansions.value = false

        when (maybeGame) {
            is Either.Right -> mutableExpansions.value = maybeGame.value
            is Either.Left -> mutableErrorMessage.value = maybeGame.value.localizedMessage ?: "Unknown Error"
        }
    }
}

sealed class UiAction
object FetchHotness : UiAction()
data class FetchGameDetails(val gameId: Long) : UiAction()
data class FetchExpansion(val gameId: Long) : UiAction()