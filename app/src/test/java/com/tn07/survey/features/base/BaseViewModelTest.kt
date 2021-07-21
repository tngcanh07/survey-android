package com.tn07.survey.features.base

import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.PublishSubject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 * Created by toannguyen
 * Jul 21, 2021 at 21:16
 */
internal class BaseViewModelTest {
    private lateinit var viewModel: BaseViewModelTestImpl

    @BeforeEach
    fun setUp() {
        viewModel = BaseViewModelTestImpl()
    }

    @Test
    fun `test add and clear disposable`() {
        val subject = PublishSubject.create<String>()
        val disposable = subject.subscribe()

        viewModel.addDisposable(disposable)
        Assertions.assertFalse(disposable.isDisposed)

        viewModel.clear()
        Assertions.assertTrue(disposable.isDisposed)
    }

    @Test
    fun `test add disposable after clearing`() {
        val subject = PublishSubject.create<String>()
        val disposable = subject.subscribe()

        viewModel.clear()
        viewModel.addDisposable(disposable)
        Assertions.assertFalse(disposable.isDisposed)
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