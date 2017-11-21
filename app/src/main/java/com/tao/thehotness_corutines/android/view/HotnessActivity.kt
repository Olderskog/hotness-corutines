package com.tao.thehotness_corutines.android.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.tao.thehotness_corutines.R
import com.tao.thehotness_corutines.android.Android
import com.tao.thehotness_corutines.android.HotnessAdapter
import com.tao.thehotness_corutines.android.HotnessViewModel
import com.tao.thehotness_corutines.android.appComponent
import com.tao.thehotness_corutines.data.toDomain
import com.tao.thehotness_corutines.di.ActivityComponent
import com.tao.thehotness_corutines.di.modules.activity.ActivityModule
import kotlinx.android.synthetic.main.activity_hotness.*
import kotlinx.coroutines.experimental.async
import ru.gildor.coroutines.retrofit.Result
import javax.inject.Inject


class HotnessActivity : AppCompatActivity() {

    private val component: ActivityComponent by lazy { appComponent()
                                                .newActivityComponent(ActivityModule(this)) }

    @Inject lateinit var viewModel: HotnessViewModel
    @Inject lateinit var adapter: HotnessAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotness)
        component.inject(this)

        initGui()
        setupListeners()

        refreshList()
    }

    private fun initGui() {
        hotness_recycler_view.layoutManager = LinearLayoutManager(this)
        hotness_recycler_view.adapter = adapter.apply { clickListener = { fetchGameInfo(it.gameId) } }
    }

    private fun setupListeners() {
        hotness_swipe_refresh_layout.setOnRefreshListener { refreshList() }
    }

    private fun refreshList() = async(Android) {
        hotness_swipe_refresh_layout.isRefreshing = true
        val hotnessResult = viewModel.fetchHotness()
        hotness_swipe_refresh_layout.isRefreshing = false

        when(hotnessResult) {
            is Result.Ok -> adapter.games = hotnessResult.value.toDomain()
            is Result.Error -> Log.e("HotnessActivity", "Error: ${hotnessResult.exception.localizedMessage}", hotnessResult.exception)
            is Result.Exception -> Log.e("HotnessActivity", "Exception: ${hotnessResult.exception.localizedMessage}", hotnessResult.exception)
        }
    }

    private fun fetchGameInfo(gameId: Long) = async(Android) {
        val gameInfoResult = viewModel.fetchGameDetails(gameId)
        when(gameInfoResult) {
            is Result.Ok -> Log.i("HotnessActivity", "Got GameDetails")
            is Result.Error -> Log.e("HotnessActivity", "Error: ${gameInfoResult.exception.localizedMessage}", gameInfoResult.exception)
            is Result.Exception -> Log.e("HotnessActivity", "Exception: ${gameInfoResult.exception.localizedMessage}", gameInfoResult.exception)
        }
    }

}
