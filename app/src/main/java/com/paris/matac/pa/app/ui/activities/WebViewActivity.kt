package com.paris.matac.pa.app.ui.activities

import android.os.Bundle
import com.paris.matac.pa.app.application.WebViewApplication
import com.paris.matac.pa.app.databinding.ActivityWebViewBinding
import com.paris.matac.pa.app.repository.Repository
import com.paris.matac.pa.app.viewbinding.ActivityViewBinding
import javax.inject.Inject

class WebViewActivity : ActivityViewBinding<ActivityWebViewBinding>(ActivityWebViewBinding::inflate) {

  @Inject lateinit var repository: Repository

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    inject()
  }

  private fun inject(): Unit = (application as WebViewApplication).appComponent.inject(this)

  companion object {

    private const val TAG: String = "WebViewActivity"
  }
}