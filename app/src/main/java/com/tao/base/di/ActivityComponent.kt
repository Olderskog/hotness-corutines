package com.tao.base.di

import com.tao.base.android.view.HotnessActivity
import com.tao.base.di.modules.activity.ActivityModule
import dagger.Subcomponent


@ActivityScope
@Subcomponent(modules = [(ActivityModule::class)])
interface ActivityComponent {

    fun inject(activity: HotnessActivity)

}

@ActivityScope
annotation class ActivityScope
