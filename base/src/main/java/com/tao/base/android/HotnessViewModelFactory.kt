package com.tao.base.android

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.tao.base.domain.GameRepository
import javax.inject.Inject


class HotnessViewModelFactory
    @Inject constructor(private val gameRepository: GameRepository): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HotnessViewModel(gameRepository) as T
    }
}