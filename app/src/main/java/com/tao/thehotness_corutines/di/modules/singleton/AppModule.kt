package com.tao.thehotness_corutines.di.modules.singleton

import android.content.Context
import com.squareup.moshi.Moshi
import com.tao.thehotness_corutines.android.BggApplication
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Singleton


@Module
class AppModule(private val application: BggApplication) {

    @Provides @AppContext
    fun context(): Context {
        return application
    }

    @Module
    companion object {

        @JvmStatic @Provides @Singleton
        fun moshi() = Moshi.Builder().build()

    }

}

@Qualifier
annotation class AppContext