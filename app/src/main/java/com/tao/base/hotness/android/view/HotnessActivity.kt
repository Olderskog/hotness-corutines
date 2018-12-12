package com.tao.base.hotness.android.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.Toast
import com.tao.base.R
import com.tao.base.hotness.android.FetchHotness
import com.tao.base.hotness.android.HotnessAdapter
import com.tao.base.hotness.android.HotnessViewModel
import com.tao.base.hotness.android.HotnessViewModelFactory
import com.tao.base.base.utils.appComponent
import com.tao.base.di.ActivityComponent
import com.tao.base.di.modules.activity.ActivityModule
import kotlinx.android.synthetic.main.activity_hotness.*
import javax.inject.Inject


class HotnessActivity : BaseActivity() {

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

        viewModel.action(FetchHotness)
    }

    private fun initGui() {
        supportActionBar?.title = "Top 100"

        hotness_recycler_view.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        hotness_recycler_view.adapter = adapter.apply {
            clickListener = {
                startActivity(DetailsActivity.launchIntent(this@HotnessActivity, it.gameId))
            }
        }
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
