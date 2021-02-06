package com.chandhu.neetprephiringchallenge.datamodels

data class NewsResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)