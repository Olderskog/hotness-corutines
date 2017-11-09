package com.tao.thehotness_corutines.android

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.tao.thehotness_corutines.R
import com.tao.thehotness_corutines.data.BGGService
import com.tao.thehotness_corutines.data.toDomain
import com.tao.thehotness_corutines.di.ActivityComponent
import com.tao.thehotness_corutines.di.modules.activity.ActivityModule
import com.tao.thehotness_corutines.domain.Android
import kotlinx.android.synthetic.main.activity_hotness.*
import kotlinx.coroutines.experimental.async
import ru.gildor.coroutines.retrofit.Result
import ru.gildor.coroutines.retrofit.awaitResult
import javax.inject.Inject


class HotnessActivity : AppCompatActivity() {

    val component: ActivityComponent by lazy { appComponent()
                                                .newActivityComponent(ActivityModule(this)) }

    @Inject lateinit var service: BGGService
    @Inject lateinit var adapter: HotnessAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotness)
        component.inject(this)

        initGui()
        setupListeners()
    }

    private fun initGui() {
        hotness_recycler_view.layoutManager = LinearLayoutManager(this)
        hotness_recycler_view.adapter = adapter
    }

    private fun setupListeners() {
        hotness_swipe_refresh_layout.setOnRefreshListener { refreshList() }
    }

    private fun refreshList() = async(Android) {
        val hotnessResult = service.getHotness().awaitResult()
        hotness_swipe_refresh_layout.isRefreshing = false
        when(hotnessResult) {
            is Result.Ok -> adapter.games = hotnessResult.value.toDomain()
            is Result.Error -> Log.e("HotnessActivity", "getHotness - Error: ${hotnessResult.exception}")
            is Result.Exception -> Log.e("HotnessActivity", "getHotness - Exception: ${hotnessResult.exception}", hotnessResult.exception)
        }
    }

}
