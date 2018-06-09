package com.tao.base.di.modules.singleton

import android.content.Context
import com.tao.base.base.BggApplication
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier


@Module
class AppModule(private val application: BggApplication) {

    @Provides @AppContext
    fun context(): Context = application
}

@Qualifier
annotation class AppContext