package com.tao.datasource.remote.di

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import glt.com.datasource.BuildConfig
import com.tao.datasource.remote.BGGService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton


@Module
object NetworkModule {

    /*
    @JvmStatic @Provides @Chuck
    fun chuckInterceptor(@AppContext context: Context) : Interceptor {
        return ChuckInterceptor(context)
                .showNotification(false)
    }
    */

    @JvmStatic
    private fun loggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = when {
                BuildConfig.DEBUG -> HttpLoggingInterceptor.Level.BODY
                else              -> HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    @JvmStatic
    private fun headerInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
            val modifiedRequest = request.newBuilder()
                    .header("Content-Type", "application/json;charset=UTF-8")
                    .build()

            chain.proceed(modifiedRequest)
        }
    }

    @JvmStatic
    private fun httpClient(/* @Chuck chuckInterceptor: Interceptor */): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor())
                .addInterceptor(headerInterceptor())
                //.addInterceptor(chuckInterceptor)
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .build()
    }

    @JvmStatic
    private fun retrofit(): Retrofit {
        return Retrofit.Builder()
                .client(httpClient())
                .baseUrl("https://bgg-json.azurewebsites.net")
                .addConverterFactory(MoshiConverterFactory.create(NetworkModule.moshi()))
                .build()
    }


    /* PROVIDERS */

    @JvmStatic @Provides @Singleton
    fun moshi(): Moshi = Moshi.Builder().build()

    @JvmStatic @Provides @Singleton
    fun service(): BGGService = retrofit().create(BGGService::class.java)

}

@Qualifier
annotation class Chuck
