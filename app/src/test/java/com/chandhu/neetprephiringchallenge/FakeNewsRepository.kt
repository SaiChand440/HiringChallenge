package com.chandhu.neetprephiringchallenge


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chandhu.neetprephiringchallenge.datamodels.Article
import com.chandhu.neetprephiringchallenge.repository.NewsRepository

class FakeNewsRepository : NewsRepository() {

    private val newsItems = mutableListOf<Article>()

    private val observableNewsItems = MutableLiveData<List<Article>>()

    private var shouldReturnNetworkError = false

    fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    private fun refreshLiveData() {
        observableNewsItems.postValue(newsItems)
    }


}
