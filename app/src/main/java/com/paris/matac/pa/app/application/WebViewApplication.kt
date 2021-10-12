package com.paris.matac.pa.app.application

import android.app.Application
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.onesignal.OneSignal
import com.paris.matac.pa.app.BuildConfig
import com.paris.matac.pa.app.di.AppComponent
import com.paris.matac.pa.app.di.DaggerAppComponent
import com.paris.matac.pa.app.di.RepositoryModule
import com.paris.matac.pa.app.models.AFStatus
import com.paris.matac.pa.app.models.BinomLink
import com.paris.matac.pa.app.repository.Repository
import com.paris.matac.pa.app.util.decodeFromBase64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class WebViewApplication : Application() {

  lateinit var appComponent: AppComponent
  @Inject lateinit var repository: Repository

  lateinit var googleAdvertisingId: String
  lateinit var appsFlyerId: String

  override fun onCreate() {
    super.onCreate()

    setupDI()
    inject()
    setupGoogleAdvertisingId()
    setupOneSignal()
    setupAppsFlyer()
  }

  private fun setupDI() {
    appComponent = DaggerAppComponent.builder()
      .repositoryModule(RepositoryModule(applicationContext))
      .build()
  }

  private fun inject(): Unit = appComponent.inject(this)

  private fun setupGoogleAdvertisingId() {
    MobileAds.initialize(applicationContext)
    GlobalScope.launch(Dispatchers.IO) {
      googleAdvertisingId = AdvertisingIdClient.getAdvertisingIdInfo(applicationContext).id
    }
  }

  private fun setupOneSignal() {
    OneSignal.initWithContext(applicationContext)
    OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
    OneSignal.setAppId(BuildConfig.ONE_SIGNAL_KEY_MOTAL.decodeFromBase64())
  }

  private fun setupAppsFlyer() {
    val appsFlyerConversionListener = object : AppsFlyerConversionListener {
      override fun onConversionDataFail(p0: String?) {}
      override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {}
      override fun onAttributionFailure(p0: String?) {}

      override fun onConversionDataSuccess(dataMap: MutableMap<String, Any>?) {
        val sub10 =
          "$appsFlyerId||$googleAdvertisingId||${BuildConfig.APPS_FLYER_KEY_MOTAL.decodeFromBase64()}"
        val binomLink = when (dataMap?.get(AF_STATUS)) {
          ORGANIC -> {
            BinomLink(
              afStatus = AFStatus.ORGANIC, base = null, key = null,
              bundle = BuildConfig.APPLICATION_ID,
              sub2 = null, sub3 = null, sub4 = null, sub5 = null, sub6 = null,
              sub7 = ORGANIC, sub10 = sub10
            )
          }
          NON_ORGANIC -> {
            val campaignTurin = dataMap[CAMPAIGN]?.toString()?.split("||")?.map { it.substringAfter(':') }
            BinomLink(
              afStatus = AFStatus.NON_ORGANIC, base = null, key = campaignTurin?.getOrNull(1),
              bundle = BuildConfig.APPLICATION_ID,
              sub2 = campaignTurin?.getOrNull(2), sub3 = campaignTurin?.getOrNull(3),
              sub4 = dataMap[AD_GROUP]?.toString(), sub5 = dataMap[AD_SET]?.toString(),
              sub6 = dataMap[AF_CHANNEL]?.toString(), sub7 = dataMap[MEDIA_SOURCE]?.toString(),
              sub10 = sub10
            )
          }
          else -> {
            null
          }
        }
        repository.binomLink = binomLink
      }
    }
    AppsFlyerLib.getInstance().apply {
      init(BuildConfig.APPS_FLYER_KEY_MOTAL.decodeFromBase64(), appsFlyerConversionListener, applicationContext)
      start(applicationContext)
      appsFlyerId = getAppsFlyerUID(applicationContext)
    }
  }

  companion object {

    //Keys for appsFlyerConversionListener
    private const val AF_STATUS: String = "af_status"
    private const val ORGANIC: String = "Organic"
    private const val NON_ORGANIC: String = "Non-organic"
    private const val CAMPAIGN: String = "campaign"
    private const val AD_GROUP: String = "adgroup"
    private const val AD_SET: String = "adset"
    private const val AF_CHANNEL: String = "af_channel"
    private const val MEDIA_SOURCE: String = "media_source"
  }
}