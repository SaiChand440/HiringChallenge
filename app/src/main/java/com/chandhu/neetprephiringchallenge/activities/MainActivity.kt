package com.chandhu.neetprephiringchallenge.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.chandhu.neetprephiringchallenge.R
import com.chandhu.neetprephiringchallenge.adapters.NewsAdapter
import com.chandhu.neetprephiringchallenge.api.RetrofitInstance
import com.chandhu.neetprephiringchallenge.utils.Constants.Companion.API_KEY
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var newsAdapter : NewsAdapter

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
            rvNewsArticles.apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
                newsAdapter = NewsAdapter()
                adapter = newsAdapter
            }
            newsAdapter.differ.submitList(response.body()?.articles)
            newsAdapter.setOnItemClickListener {
                val intent : Intent = Intent(this, WebActivity::class.java)
                intent.putExtra("URL",it.url)
                startActivity(intent)
            }
        } else {
            Log.e("Random",response.errorBody().toString())
        }
    }
}