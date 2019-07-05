package com.danielecampogiani.retry

import android.app.Application
import android.content.Context
import com.danielecampogiani.retry.inject.AppComponent
import com.danielecampogiani.retry.inject.AppModule
import com.danielecampogiani.retry.inject.DaggerAppComponent

class RetryApplication : Application() {

    companion object {
        fun getAppComponent(context: Context): AppComponent {
            val app = context.applicationContext as RetryApplication
            return app.appComponent
        }
    }


    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }
}