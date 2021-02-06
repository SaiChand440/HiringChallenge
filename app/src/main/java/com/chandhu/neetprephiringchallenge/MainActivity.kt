package com.chandhu.neetprephiringchallenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.chandhu.neetprephiringchallenge.api.NewsAPI
import com.chandhu.neetprephiringchallenge.api.RetrofitInstance
import com.chandhu.neetprephiringchallenge.datamodels.NewsResponse
import com.chandhu.neetprephiringchallenge.utils.Constants.Companion.API_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GlobalScope.launch(Dispatchers.Main) {
            getNews()
        }
    }

    private suspend fun getNews() {
        val response = RetrofitInstance.api.getNews("in",1, API_KEY)
        if(response.isSuccessful) {
            Log.e("Random",response.body().toString())
        } else {
            Log.e("Random",response.errorBody().toString())
        }
    }
}