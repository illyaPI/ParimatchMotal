package com.paris.matac.pa.app.di

import android.content.Context
import com.google.gson.Gson
import com.paris.matac.pa.app.repository.Repository
import com.paris.matac.pa.app.repository.WebViewRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule(
  private val appContext: Context,
) {

  @Provides
  fun provideAppContext(): Context = appContext

  @Provides
  fun provideGson(): Gson = Gson()

  @Singleton
  @Provides
  fun provideRepository(appContext: Context, gson: Gson): Repository = WebViewRepository(appContext, gson)
}