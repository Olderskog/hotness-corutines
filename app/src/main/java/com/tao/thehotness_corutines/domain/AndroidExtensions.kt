package com.tao.thehotness_corutines.domain

import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


fun ViewGroup.inflate(@LayoutRes layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).run {
        inflate(layoutId, this@inflate, attachToRoot)
    }
}