package com.paris.matac.pa.app.repository

import com.paris.matac.pa.app.models.BinomLink

interface Repository {

  var binomLink: BinomLink?
  var whiteBase: String
  var blackBase: String?
  var defaultKey: String

  var lastBinomLink: String?
}