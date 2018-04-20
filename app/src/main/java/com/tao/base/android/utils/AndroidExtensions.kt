package com.tao.base.android.utils

import android.animation.ObjectAnimator
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tao.base.android.BggApplication


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

// TODO: Scale the duration according to linenumbers
fun TextView.toggleExpansion(minLines: Int = 3) {
    val duration = (lineCount - minLines) * 10
    if (maxLines == minLines)
        expand(duration = duration.toLong())
    else
        collapse(minLines, duration.toLong())
}

/*


private void cycleTextViewExpansion(TextView tv){
    int collapsedMaxLines = 3;
    ObjectAnimator animation = ObjectAnimator.ofInt(tv, "maxLines",
        tv.getMaxLines() == collapsedMaxLines? tv.getLineCount() : collapsedMaxLines);
    animation.setDuration(200).start();
}

 */