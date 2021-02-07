package com.chandhu.neetprephiringchallenge.ui

import com.google.common.truth.Truth
import okhttp3.internal.EMPTY_RESPONSE
import org.junit.Before

class NewsViewModelTest {
    private lateinit var viewModel: NewsViewModel

    fun checkEmptyNews() {
        val ans = viewModel.getNews("")
        Truth.assertThat(ans).isNull()
    }

    fun checkCountryNews() {
        val ans = viewModel.getNews("in")
        Truth.assertThat(ans).isNotNull()
    }


}