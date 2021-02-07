package com.chandhu.neetprephiringchallenge.ui

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chandhu.neetprephiringchallenge.R
import com.chandhu.neetprephiringchallenge.adapters.NewsAdapter
import com.chandhu.neetprephiringchallenge.api.RetrofitInstance
import com.chandhu.neetprephiringchallenge.repository.NewsRepository
import com.chandhu.neetprephiringchallenge.utils.Constant.Companion.API_KEY
import com.chandhu.neetprephiringchallenge.utils.Constant.Companion.QUERY_PAGE_SIZE
import com.chandhu.neetprephiringchallenge.utils.Resource
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Cache


class MainActivity : AppCompatActivity() {

    private lateinit var newsAdapter: NewsAdapter
    private lateinit var viewModel: NewsViewModel

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val newsRepository = NewsRepository()
        val viewModelFactory = NewsViewModelFactory(newsRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(NewsViewModel::class.java)

        viewModel.news.observe(this, Observer {
            when (it) {
                is Resource.Success -> {
                    shimmerFrameLayout.stopShimmer()
                    shimmerFrameLayout.visibility = View.GONE
                    error_button.visibility = View.GONE
                    hideProgressBar()
                    it.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.newsPage == totalPages
                        if (isLastPage) {
                            rvNewsArticles.setPadding(0, 0, 0, 0)
                        }
                    }
                }
                is Resource.Error -> {
                    shimmerFrameLayout.stopShimmer()
                    shimmerFrameLayout.visibility = View.GONE
                    hideProgressBar()
                    it.message?.let { message ->
                        error_button.text = message
                        error_button.visibility = View.VISIBLE
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

        swipeContainer.setOnRefreshListener {
            doNetworkRequest()
        }

        swipeContainer.setColorSchemeColors(
            getColor(android.R.color.holo_blue_light),
            getColor(android.R.color.holo_green_light),
            getColor(android.R.color.holo_orange_light),
            getColor(android.R.color.holo_red_light)
        )

        setUpRecyclerView()

        newsAdapter.setOnItemClickListener {
            intent = Intent(this, WebActivity::class.java)
            intent.putExtra("URL", it.url)
            startActivity(intent)
        }
    }

    private fun doNetworkRequest() {
        viewModel.news.observe(this, Observer {
            when (it) {
                is Resource.Success -> {
                    shimmerFrameLayout.stopShimmer()
                    shimmerFrameLayout.visibility = View.GONE
                    error_button.visibility = View.GONE
                    hideProgressBar()
                    it.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        swipeContainer.isRefreshing = false
                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.newsPage == totalPages
                        if (isLastPage) {
                            rvNewsArticles.setPadding(0, 0, 0, 0)
                        }
                    }
                }
                is Resource.Error -> {
                    shimmerFrameLayout.stopShimmer()
                    shimmerFrameLayout.visibility = View.GONE
                    hideProgressBar()
                    swipeContainer.isRefreshing = false
                    it.message?.let { message ->
                        error_button.text = message
                        error_button.visibility = View.VISIBLE
                    }
                }
                is Resource.Loading -> {
                    swipeContainer.isRefreshing = false
                    showProgressBar()
                }
            }
        })
    }

    private fun hideProgressBar() {
//        progress_circular.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
//        progress_circular.visibility = View.VISIBLE
        isLoading = true
    }

    private fun setUpRecyclerView() {
        newsAdapter = NewsAdapter()
        rvNewsArticles.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            addOnScrollListener(this@MainActivity.scrollListener)
        }
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                viewModel.getNews("us")
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

}