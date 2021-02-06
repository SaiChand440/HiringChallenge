package com.chandhu.neetprephiringchallenge.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.chandhu.neetprephiringchallenge.R
import com.chandhu.neetprephiringchallenge.adapters.NewsAdapter
import com.chandhu.neetprephiringchallenge.api.RetrofitInstance
import com.chandhu.neetprephiringchallenge.repository.NewsRepository
import com.chandhu.neetprephiringchallenge.utils.Constant.Companion.API_KEY
import com.chandhu.neetprephiringchallenge.utils.Resource
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var newsAdapter : NewsAdapter
    private lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val newsRepository = NewsRepository()
        val viewModelFactory = NewsViewModelFactory(newsRepository)
        viewModel = ViewModelProvider(this,viewModelFactory).get(NewsViewModel::class.java)

        viewModel.news.observe(this, Observer {
            when(it){
                is Resource.Success -> {
                    hideProgressBar()
                    it.data?.let {newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    it.message?.let {message ->
                        Toast.makeText(this,message,Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

        setUpRecyclerView()
    }

    private fun hideProgressBar() {
        progress_circular.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        progress_circular.visibility = View.VISIBLE
    }

    private fun setUpRecyclerView() {
        newsAdapter = NewsAdapter()
        rvNewsArticles.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

}