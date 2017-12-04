package com.tao.base.di

import com.tao.base.di.modules.activity.ActivityModule
import com.tao.base.di.modules.singleton.NetworkModule
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [(NetworkModule::class)])
interface AppComponent {

    fun newActivityComponent(activityModule: ActivityModule): ActivityComponent

}
