package com.tao.base.base.utils

import android.animation.ObjectAnimator
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tao.base.base.BggApplication


fun AppCompatActivity.appComponent() = (application as BggApplication).appComponent

fun ViewGroup.inflate(@LayoutRes layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).run {
        inflate(layoutId, this@inflate, attachToRoot)
    }
}

fun TextView.expand(duration: Long = 200L) {
    try {
        ObjectAnimator.ofInt(this, "maxLines", lineCount)
                .setDuration(duration)
                .start()
    } catch (e: Exception) {
        Log.e("TextviewExt", "Expansion failed with error: ", e)
    }
}

fun TextView.collapse(numLines: Int, duration: Long = 200L) {
    try {
        ObjectAnimator.ofInt(this, "maxLines", numLines)
                .setDuration(duration)
                .start()
    } catch (e: Exception) {
        Log.e("TextviewExt", "Collapsing failed with error: ", e)
    }
}
