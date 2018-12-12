package com.tao.base.hotness.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tao.base.hotness.domain.GameRepository
import javax.inject.Inject


class HotnessViewModelFactory
    @Inject constructor(private val gameRepository: GameRepository): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HotnessViewModel(gameRepository) as T
    }
}