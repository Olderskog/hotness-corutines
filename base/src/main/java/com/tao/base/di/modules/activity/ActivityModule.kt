package com.tao.base.di.modules.activity

import android.content.Context
import android.support.v7.app.AppCompatActivity
import dagger.Module
import dagger.Provides


@Module
class ActivityModule(private val activity: AppCompatActivity) {

    @Provides
    fun context(): Context = activity
}