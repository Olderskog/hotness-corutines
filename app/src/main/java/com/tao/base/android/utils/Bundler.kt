package com.tao.base.android.utils

import android.os.Bundle

interface Bundler {

    fun getBundleService(): BundleService

}

class BundleService(savedState: Bundle?, intentExtra: Bundle?) {

    val data: Bundle = Bundle()

    init {
        savedState?.let { data.putAll(it) }
        intentExtra?.let { data.putAll(it) }
    }

}