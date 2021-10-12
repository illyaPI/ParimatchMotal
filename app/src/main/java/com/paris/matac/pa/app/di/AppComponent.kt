package com.paris.matac.pa.app.di

import com.paris.matac.pa.app.application.WebViewApplication
import com.paris.matac.pa.app.ui.activities.LoadingActivity
import com.paris.matac.pa.app.ui.activities.WebViewActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RepositoryModule::class])
interface AppComponent {

  fun inject(application: WebViewApplication)
  fun inject(activity: LoadingActivity)
  fun inject(activity: WebViewActivity)
}