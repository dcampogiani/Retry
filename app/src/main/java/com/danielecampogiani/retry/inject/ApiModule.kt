package com.danielecampogiani.retry.inject

import com.danielecampogiani.retry.detail.api.JokesAPI
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal class ApiModule {


    @Provides
    @Singleton
    internal fun provideJokesApi(): JokesAPI {
        return JokesAPI()
    }

}
