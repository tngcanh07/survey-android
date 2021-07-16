package com.tn07.survey.features.base

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

/**
 * Created by toannguyen
 * Jul 16, 2021 at 13:58
 */
open class BaseViewModel : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    @Synchronized
    fun Disposable.addToCompositeDisposable() {
        compositeDisposable.add(this)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}