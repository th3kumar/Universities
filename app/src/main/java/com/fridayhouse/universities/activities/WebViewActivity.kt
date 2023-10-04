package com.fridayhouse.universities.activities

import android.os.Bundle
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.fridayhouse.universities.R

class WebViewActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        val domain = intent.getStringExtra("domain")

        webView = findViewById(R.id.webView)


        Log.d("WebViewActivity", "Domain received: $domain")  // Log the received domain

        // Set a custom WebViewClient to handle page navigation
        webView.webViewClient = MyWebViewClient()

        // Configure WebView settings
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true // Enable JavaScript if needed
        webSettings.allowContentAccess = true
        webSettings.domStorageEnabled = true


        // Load the URL within the app's WebView
        // Check if the domain starts with "http://" or "https://"
        if (domain != null) {
            if (!domain.startsWith("http://") && !domain.startsWith("https://")) {
                // If it doesn't have a prefix, add "https://"
                val fullUrl = "https://$domain"
                Log.d("WebViewActivity", "Loading URL: $fullUrl")  // Log the URL being loaded
                webView.loadUrl(fullUrl)
            } else {
                // If it already has a prefix, load it as is
                Log.d("WebViewActivity", "Loading URL: $domain")  // Log the URL being loaded
                webView.loadUrl(domain)
            }
        }
    }

    // Custom WebViewClient to handle page navigation
    private inner class MyWebViewClient() : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            // Return false to load the URL within the WebView
            return true
        }
    }
}