package com.tn07.survey.features.base

import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.PublishSubject
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * Created by toannguyen
 * Jul 21, 2021 at 21:16
 */
internal class BaseViewModelTest {
    private lateinit var viewModel: BaseViewModelTestImpl

    @Before
    fun setUp() {
        viewModel = BaseViewModelTestImpl()
    }

    @Test
    fun `test add and clear disposable`() {
        val subject = PublishSubject.create<String>()
        val disposable = subject.subscribe()

        viewModel.addDisposable(disposable)
        Assert.assertFalse(disposable.isDisposed)

        viewModel.clear()
        Assert.assertTrue(disposable.isDisposed)
    }

    @Test
    fun `test add disposable after clearing`() {
        val subject = PublishSubject.create<String>()
        val disposable = subject.subscribe()

        viewModel.clear()
        viewModel.addDisposable(disposable)
        Assert.assertFalse(disposable.isDisposed)
    }
}

private open class BaseViewModelTestImpl() : BaseViewModel() {
    fun addDisposable(disposable: Disposable) {
        disposable.addToCompositeDisposable()
    }

    fun clear() {
        onCleared()
    }
}