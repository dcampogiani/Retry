package com.danielecampogiani.retry.inject

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.danielecampogiani.retry.connection.ConnectionChecker
import com.danielecampogiani.retry.connection.ConnectionCheckerImpl
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val application: Application) {

    @Provides
    fun provideContext() = application

    @Provides
    fun provideConnectivityManager(application: Application): ConnectivityManager =
        application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Provides
    fun provideConnectionChecker(impl: ConnectionCheckerImpl): ConnectionChecker = impl

}