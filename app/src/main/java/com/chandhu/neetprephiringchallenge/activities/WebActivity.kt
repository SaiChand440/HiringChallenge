package com.chandhu.neetprephiringchallenge.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.chandhu.neetprephiringchallenge.R
import kotlinx.android.synthetic.main.activity_web.*

class WebActivity : AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        val url = intent.getStringExtra("URL")

        webView.settings.userAgentString = "Android"
        webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        if (url != null) {
            webView.settings.javaScriptEnabled = true
            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    progressBar.visibility = View.INVISIBLE
                    webView.visibility = View.VISIBLE
                }
            }

            webView.loadUrl(url)
        }
    }
}