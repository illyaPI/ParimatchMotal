package com.paris.matac.pa.app.repository

import android.content.Context
import com.google.gson.Gson
import com.paris.matac.pa.app.models.BinomLink
import com.paris.matac.pa.app.util.initializationError

class WebViewRepository(
  appContext: Context,
  private val gson: Gson,
) : Repository {

  private val prefs = appContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

  private fun getString(keyTurin: String): String? = prefs.getString(keyTurin, null)

  private fun putString(keyTurin: String, valueTurin: String?): Unit =
    prefs.edit().putString(keyTurin, valueTurin).apply()

  override var binomLink: BinomLink?
    get() {
      val binomLinkStringTurin = prefs.getString(BINOM_LINK, null) ?: return null
      return gson.fromJson(binomLinkStringTurin, BinomLink::class.java)
    }
    set(value) = prefs.edit().putString(BINOM_LINK, gson.toJson(value)).apply()

  override var whiteBase: String
    get() = getString(WHITE_BASE) ?: initializationError("whiteBase")
    set(value) = putString(WHITE_BASE, value)

  override var blackBase: String?
    get() = getString(BLACK_BASE)
    set(value) = putString(BLACK_BASE, value)

  override var defaultKey: String
    get() = getString(DEFAULT_KEY) ?: initializationError("defaultKey")
    set(value) = putString(DEFAULT_KEY, value)

  override var lastBinomLink: String?
    get() = getString(LAST_BINOM_LINK)
    set(value) = putString(LAST_BINOM_LINK, value)

  companion object {

    private const val PREFS: String = "PREFS"

    private const val BINOM_LINK: String = "BINOM_LINK"
    private const val WHITE_BASE: String = "WHITE_BASE"
    private const val BLACK_BASE: String = "BLACK_BASE"
    private const val DEFAULT_KEY: String = "DEFAULT_KEY"

    private const val LAST_BINOM_LINK: String = "LAST_BINOM_LINK"
  }
}