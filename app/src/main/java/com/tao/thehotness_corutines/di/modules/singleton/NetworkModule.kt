package com.tao.thehotness_corutines.di.modules.singleton

import com.squareup.moshi.Moshi
import com.tao.thehotness_corutines.BuildConfig
import com.tao.thehotness_corutines.data.BGGService
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module(includes = arrayOf(AppModule::class))
object NetworkModule {

    @JvmStatic @Provides @Singleton
    fun moshi(): Moshi = Moshi.Builder().build()

    @JvmStatic @Provides
    fun loggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = when {
                BuildConfig.DEBUG -> HttpLoggingInterceptor.Level.BODY
                else -> HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    @JvmStatic @Provides
    fun headerInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
            val modifiedRequest = request.newBuilder()
                                            .header("Content-Type", "application/json;charset=UTF-8")
                                            .build()

            chain.proceed(modifiedRequest)
        }
    }

    @JvmStatic @Provides @Singleton
    fun httpClient(loggingInterceptor: HttpLoggingInterceptor,
                   headerInterceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
                            .addInterceptor(loggingInterceptor)
                            .addInterceptor(headerInterceptor)
                            .connectTimeout(5, TimeUnit.SECONDS)
                            .readTimeout(5, TimeUnit.SECONDS)
                            .build()
    }

    @JvmStatic @Provides @Singleton
    fun retrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit {
        return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://bgg-json.azurewebsites.net")
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
    }

    @JvmStatic @Provides @Singleton
    fun service(retrofit: Retrofit): BGGService = retrofit.create(BGGService::class.java)

}
