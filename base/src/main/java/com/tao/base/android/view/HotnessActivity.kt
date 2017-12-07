package com.tao.base.android.view

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.tao.base.R
import com.tao.base.android.*
import com.tao.base.di.ActivityComponent
import com.tao.base.di.modules.activity.ActivityModule
import kotlinx.android.synthetic.main.activity_hotness.*
import javax.inject.Inject


class HotnessActivity : AppCompatActivity() {

    private val component: ActivityComponent by lazy { appComponent()
                                                .newActivityComponent(ActivityModule(this)) }

    private val viewModel by lazy { ViewModelProviders.of(this, viewModelFactory)
                                                .get(HotnessViewModel::class.java)}

    @Inject lateinit var viewModelFactory: HotnessViewModelFactory
    @Inject lateinit var adapter: HotnessAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotness)
        component.inject(this)

        initGui()
        setupListeners()
        observeChanges()

        //if (!isChangingConfigurations)
            viewModel.action(FetchHotness)
    }

    private fun initGui() {
        hotness_recycler_view.layoutManager = LinearLayoutManager(this)
        hotness_recycler_view.adapter = adapter.apply { clickListener = { viewModel.action(FetchGameDetails(it.gameId)) } }
    }

    private fun setupListeners() {
        hotness_swipe_refresh_layout.setOnRefreshListener { viewModel.action(FetchHotness) }
    }

    private fun observeChanges() {
        viewModel.errorMessage.observe { if (it != null && it.isNotEmpty()) Toast.makeText(this, it, Toast.LENGTH_LONG).show() }
        viewModel.hotness.observe { adapter.games = it ?: emptyList() }
        viewModel.loading.observe { hotness_swipe_refresh_layout.isRefreshing = it ?: false }
    }

    private fun <T> LiveData<T>.observe(observe: (T?) -> Unit) = observe(this@HotnessActivity, Observer { observe(it) } )

}
