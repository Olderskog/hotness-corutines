package com.tao.base.di

import com.tao.base.di.modules.activity.ActivityModule
import com.tao.base.di.modules.singleton.AppModule
import dagger.Component
import com.tao.datasource.remote.di.NetworkModule
import javax.inject.Singleton


@Singleton
@Component(modules = [(AppModule::class), (NetworkModule::class)])
interface AppComponent {

    fun newActivityComponent(activityModule: ActivityModule): ActivityComponent

}
