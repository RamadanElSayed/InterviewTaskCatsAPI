package com.ramadan.readybackendproject.data.api

import com.ramadan.readybackendproject.data.model.CatImageDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CatApiService {

    @GET("images/search")
    suspend fun getCatImages(@Query("limit") limit: Int = 10,
                             @Header("x-api-key") apiKey: String = "live_UgXaN2xswnXXwa1LNdWUNWdVRpZJC6tOUutYxmAxflOjzEropQhMqWFjIHtYKqg9",

    ): Response<List<CatImageDto>>



}