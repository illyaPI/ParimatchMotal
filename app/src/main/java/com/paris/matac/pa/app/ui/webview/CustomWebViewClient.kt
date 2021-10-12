package com.paris.matac.pa.app.ui.webview

import android.content.Intent
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

class CustomWebViewClient(
  private val intentUrls: Array<String>,
  private val prohibitedUrls: Array<String>,
) : WebViewClient() {

  override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
    val url = request.url.toString()
    return when {
      intentUrls.find { url.startsWith(it) } != null -> {
        view.context.startActivity(Intent(Intent.ACTION_VIEW, request.url))
        true
      }
      prohibitedUrls.find { url.contains(it) } != null -> true
      else -> false
    }
  }
}