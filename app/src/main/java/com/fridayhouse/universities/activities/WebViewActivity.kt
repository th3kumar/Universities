package com.fridayhouse.universities.activities

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.fridayhouse.universities.R

class WebViewActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var lottieAnimationView: LottieAnimationView
    private lateinit var textViewGoBack: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        val domain = intent.getStringExtra("domain")

        val webView = findViewById<WebView>(R.id.webView)
        lottieAnimationView = findViewById(R.id.lottieAnimationView)
        textViewGoBack = findViewById(R.id.textViewGoBack)
        // Configure WebView settings
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true // Enable JavaScript if needed

        // Load the URL within the app's WebView
        if (domain != null) {
            webView.loadUrl(domain)
            goesBackToMainActivity()
        }


    }

    private fun goesBackToMainActivity() {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Close the WebViewActivity
    }


}