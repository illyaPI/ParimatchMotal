package com.paris.matac.pa.app.ui.webview

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView

@SuppressLint("SetJavaScriptEnabled")
class CustomWebView(
  context: Context,
  attrs: AttributeSet,
) : WebView(context, attrs) {

  init {
    scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY

    settings.apply {
      builtInZoomControls = true
      cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
      defaultTextEncodingName = "utf-8"
      displayZoomControls = false
      javaScriptEnabled = true
      useWideViewPort = true
      loadWithOverviewMode = true
      domStorageEnabled = true
      mediaPlaybackRequiresUserGesture = false
    }
  }
}