package com.tao.base.android.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import com.tao.base.android.utils.BundleService
import com.tao.base.android.utils.Bundler


@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity(), Bundler {

    private lateinit var bundleService: BundleService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bundleService = BundleService(savedInstanceState, intent.extras)
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        outState?.putAll(bundleService.data)
    }

    override fun getBundleService() = bundleService
}
