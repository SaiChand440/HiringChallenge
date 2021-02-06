package com.chandhu.neetprephiringchallenge.repository

import com.chandhu.neetprephiringchallenge.api.RetrofitInstance

class NewsRepository {
    suspend fun getNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getNews(countryCode, pageNumber)
}