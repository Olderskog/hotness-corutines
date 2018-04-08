package com.tao.base.android

import android.app.Application
import android.os.StrictMode
import com.tao.base.BuildConfig
import com.tao.base.di.AppComponent
import com.tao.base.di.DaggerAppComponent
import com.tao.base.di.modules.singleton.AppModule


class BggApplication : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
                            .appModule(AppModule(this))
                            .build()
    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG)
            initStrictMode()
    }

    private fun initStrictMode() {
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build())

        StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build())
    }

}