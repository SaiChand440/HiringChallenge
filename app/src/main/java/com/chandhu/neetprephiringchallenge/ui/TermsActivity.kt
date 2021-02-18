package com.chandhu.neetprephiringchallenge.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.chandhu.neetprephiringchallenge.R
import kotlinx.android.synthetic.main.activity_terms.*
import kotlinx.android.synthetic.main.activity_web.*

class TermsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms)

        termsAppBar.setNavigationOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        terms.setOnClickListener {
            intent = Intent(this, WebActivity::class.java)
            intent.putExtra("URL", "https://news-simplified.flycricket.io/terms.html")
            startActivity(intent)
        }

        policy.setOnClickListener {
            intent = Intent(this, WebActivity::class.java)
            intent.putExtra("URL", "https://news-simplified.flycricket.io/privacy.html")
            startActivity(intent)
        }
    }
}