package com.danielecampogiani.retry.inject

import androidx.lifecycle.ViewModel
import com.danielecampogiani.retry.detail.DetailService
import com.danielecampogiani.retry.detail.DetailServiceImpl
import com.danielecampogiani.retry.detail.DetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class DetailModule {

    @Binds
    @IntoMap
    @ViewModelKey(DetailViewModel::class)
    internal abstract fun bindMainViewModel(viewModel: DetailViewModel): ViewModel

    @Binds
    internal abstract fun bindMainService(serviceImpl: DetailServiceImpl): DetailService

}
