package com.tao.base.android

import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


fun AppCompatActivity.appComponent() = (application as BggApplication).appComponent

fun ViewGroup.inflate(@LayoutRes layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).run {
        inflate(layoutId, this@inflate, attachToRoot)
    }
}