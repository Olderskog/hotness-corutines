package com.tao.base.hotness.android.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.tao.base.base.utils.BundleService
import com.tao.base.base.utils.Bundler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext


@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity(), CoroutineScope, Bundler {

    private val parentJob = Job()
    override val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main

    private lateinit var bundleService: BundleService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bundleService = BundleService(savedInstanceState, intent.extras)
    }

    override fun onDestroy() {
        super.onDestroy()
        parentJob.cancel()
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        outState?.putAll(bundleService.data)
    }

    override fun getBundleService() = bundleService
}
