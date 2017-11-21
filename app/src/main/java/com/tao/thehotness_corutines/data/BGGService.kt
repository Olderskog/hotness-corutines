package com.tao.thehotness_corutines.data

import com.tao.thehotness_corutines.data.entities.TGame
import com.tao.thehotness_corutines.data.entities.TGameOverview
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface BGGService {

    @GET("hot")
    fun getHotness(): Call<List<TGameOverview>>

    @GET("thing/{id}")
    fun getDetails(@Path("id") gameId: Long): Call<TGame>
}