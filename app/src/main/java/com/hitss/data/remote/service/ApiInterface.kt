package com.hitss.data.remote.service

import com.hitss.data.remote.dto.response.ResponseCastTv
import com.hitss.data.remote.dto.response.ResponseDetailShow
import com.hitss.data.remote.dto.response.ResponseScheduleTv
import com.hitss.data.remote.dto.response.ResponseSearchShows
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiInterface {

    @Headers("Content-type: application/json", "Accept: application/json")
    @GET("schedule")
    suspend fun getScheduleTv(@Query("country") country : String, @Query("date") date : String) : Response<ResponseScheduleTv>

    @Headers("Content-type: application/json", "Accept: application/json")
    @GET
    suspend fun getDetailShow(@Url url : String) : Response<ResponseDetailShow>

    @Headers("Content-type: application/json", "Accept: application/json")
    @GET
    suspend fun getCastShow(@Url url : String) : Response<ResponseCastTv>

    @Headers("Content-type: application/json", "Accept: application/json")
    @GET("search/shows")
    suspend fun getShowsByQuery(@Query("q") query : String) : Response<ResponseSearchShows>

}