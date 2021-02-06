package com.chandhu.neetprephiringchallenge.api

import com.chandhu.neetprephiringchallenge.utils.Constants.Companion.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    @GET("v2/top-headlines")
    suspend fun getNews(
        @Query("country")
        countryCode: String,
        @Query("page")
        pageNum: Int = 1,
        @Query("apiKey")
        api: String = API_KEY
    ) {

    }
}