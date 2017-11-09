package com.tao.thehotness_corutines.di

import com.tao.thehotness_corutines.di.modules.activity.ActivityModule
import com.tao.thehotness_corutines.di.modules.singleton.NetworkModule
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = arrayOf(NetworkModule::class))
interface AppComponent {

    fun newActivityComponent(activityModule: ActivityModule): ActivityComponent

}
