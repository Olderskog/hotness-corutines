package com.tao.thehotness_corutines.di

import com.tao.thehotness_corutines.android.view.HotnessActivity
import com.tao.thehotness_corutines.di.modules.activity.ActivityModule
import dagger.Subcomponent


@ActivityScope
@Subcomponent(modules = arrayOf(ActivityModule::class))
interface ActivityComponent {

    fun inject(activity: HotnessActivity)

}

@ActivityScope
annotation class ActivityScope
