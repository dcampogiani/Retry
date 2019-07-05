package com.danielecampogiani.retry.detail

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.transition.TransitionManager
import com.danielecampogiani.retry.R
import com.danielecampogiani.retry.RetryApplication
import kotlinx.android.synthetic.main.activity_detail.*
import javax.inject.Inject

class DetailActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        RetryApplication.getAppComponent(this).inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val viewModel = ViewModelProviders.of(this, viewModelFactory)[DetailViewModel::class.java]

        viewModel.state.observe(this, Observer {
            TransitionManager.beginDelayedTransition(root)
            it.fold(this::onResult, this::onError, this::onLoading)
        })
    }

    private fun onResult(state: DetailState.Result) {
        loading.visibility = View.INVISIBLE
        textview.text = state.value
    }


    private fun onError(state: DetailState.Error) {
        loading.visibility = View.INVISIBLE
        textview.text = getString(state.errorId)
    }


    private fun onLoading(state: DetailState.Loading) {
        loading.visibility = View.VISIBLE
    }


}
