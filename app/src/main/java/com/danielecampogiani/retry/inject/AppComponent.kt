package com.danielecampogiani.retry.inject

import com.danielecampogiani.retry.detail.DetailActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        ViewModelBuilder::class,
        DetailModule::class,
        ApiModule::class
    ]
)
interface AppComponent {

    fun inject(detailActivity: DetailActivity)
}