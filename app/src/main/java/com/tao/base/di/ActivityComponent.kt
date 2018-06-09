package com.tao.base.di

import com.tao.base.hotness.android.view.DetailsActivity
import com.tao.base.hotness.android.view.HotnessActivity
import com.tao.base.di.modules.activity.ActivityModule
import dagger.Subcomponent


@ActivityScope
@Subcomponent(modules = [(ActivityModule::class)])
interface ActivityComponent {

    fun inject(activity: HotnessActivity)
    fun inject(activity: DetailsActivity)

}

@ActivityScope
annotation class ActivityScope
