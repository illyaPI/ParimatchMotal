package com.paris.matac.pa.app.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.paris.matac.pa.app.application.WebViewApplication
import com.paris.matac.pa.app.databinding.ActivityLoadingBinding
import com.paris.matac.pa.app.repository.Repository
import com.paris.matac.pa.app.viewbinding.ActivityViewBinding
import kotlinx.coroutines.delay
import javax.inject.Inject

class LoadingActivity : ActivityViewBinding<ActivityLoadingBinding>(ActivityLoadingBinding::inflate) {

  @Inject lateinit var repository: Repository

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    inject()
    if (repository.lastBinomLink != null) {
      startActivity(Intent(this, WebViewActivity::class.java))
      finish()
    }
    lifecycleScope.launchWhenCreated {
      delay(SETUP_DELAY)
      setupFirebase()
    }
  }

  private fun setupFirebase() {
    val firebaseConfig = FirebaseRemoteConfig.getInstance()
    firebaseConfig.fetchAndActivate().addOnCompleteListener(this) {
      val whiteBaseTurin = firebaseConfig.getString(WHITE_BASE)
      val blackBaseTurin = firebaseConfig.getString(BLACK_BASE).ifEmpty { null }
      val defaultKeyTurin = firebaseConfig.getString(DEFAULT_KEY)

      repository.whiteBase = whiteBaseTurin
      repository.blackBase = blackBaseTurin
      repository.defaultKey = defaultKeyTurin

      startActivity(Intent(this, WebViewActivityTurin::class.java))
      finish()
    }
  }

  private fun inject(): Unit = (application as WebViewApplication).appComponent.inject(this)

  companion object {

    private const val TAG: String = "LoadingActivity"

    private const val SETUP_DELAY: Long = 5000L

    private const val WHITE_BASE: String = "moswhitepage"
    private const val BLACK_BASE: String = "mosblackpage"
    private const val DEFAULT_KEY: String = "mosdefkey"
  }
}