package com.tao.datasource.remote

import com.tao.datasource.remote.entities.TGame
import com.tao.datasource.remote.entities.TGameOverview
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface BGGService {

    @GET("hot")
    fun getHotness(): Call<List<TGameOverview>>

    @GET("thing/{id}")
    fun getDetails(@Path("id") gameId: Long): Call<TGame>
}