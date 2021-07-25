package com.tn07.survey.features.forgotpassword

import com.tn07.survey.domain.usecases.RequestPasswordUseCase
import com.tn07.survey.features.base.BaseViewModel
import com.tn07.survey.features.common.SchedulerProvider
import com.tn07.survey.features.forgotpassword.transformer.ForgotPasswordTransformer
import com.tn07.survey.features.forgotpassword.uimodel.ForgotPasswordUiModel
import com.tn07.survey.features.forgotpassword.uimodel.InvalidEmailException
import com.tn07.survey.features.forgotpassword.uimodel.RequestPasswordResult
import com.tn07.survey.features.forgotpassword.validator.ForgotPasswordFormValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

/**
 * Created by toannguyen
 * Jul 25, 2021 at 14:38
 */
private typealias UpdateUiModelEvent = (ForgotPasswordUiModel) -> ForgotPasswordUiModel

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val requestPasswordUseCase: RequestPasswordUseCase,
    private val validator: ForgotPasswordFormValidator,
    private val transformer: ForgotPasswordTransformer,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel() {

    private val _forgotPasswordUiModel = BehaviorSubject.createDefault(transformer.initialUiModel)
    val forgotPasswordUiModel: Observable<ForgotPasswordUiModel>
        get() = _forgotPasswordUiModel.distinctUntilChanged()

    private val _requestPasswordResult = PublishSubject.create<RequestPasswordResult>()
    val requestPasswordResult: Observable<RequestPasswordResult>
        get() = _requestPasswordResult

    private val forgotPasswordUiModelEvents = PublishSubject.create<UpdateUiModelEvent>()

    fun init() {
        forgotPasswordUiModelEvents.toFlowable(BackpressureStrategy.BUFFER)
            .subscribeOn(schedulerProvider.io())
            .subscribe {
                _forgotPasswordUiModel.value
                    ?.let(it)
                    ?.let(_forgotPasswordUiModel::onNext)
            }
            .addToCompositeDisposable()
    }

    fun setEmail(email: String) {
        forgotPasswordUiModelEvents.onNext {
            transformer.updateEmail(it, email)
        }
    }

    fun requestPassword() {
        Single.fromCallable {
            val uiModel = _forgotPasswordUiModel.value
            validator.validateResetPasswordForm(uiModel)
            return@fromCallable uiModel.email
        }
            .doOnSubscribe { onStartRequestPassword() }
            .flatMapCompletable(requestPasswordUseCase::requestPassword)
            .subscribeOn(schedulerProvider.io())
            .subscribe(::onRequestPasswordSuccess, ::onRequestPasswordError)
            .addToCompositeDisposable()
    }

    private fun onStartRequestPassword() {
        forgotPasswordUiModelEvents.onNext {
            transformer.updateLoadingStatus(it, true)
        }
    }

    private fun onRequestPasswordSuccess() {
        forgotPasswordUiModelEvents.onNext {
            transformer.updateLoadingStatus(it, false)
        }
        _requestPasswordResult.onNext(RequestPasswordResult.Success)
    }

    private fun onRequestPasswordError(throwable: Throwable) {
        forgotPasswordUiModelEvents.onNext {
            transformer.updateLoadingStatus(it, false)
        }
        if (throwable is InvalidEmailException) {
            forgotPasswordUiModelEvents.onNext {
                transformer.updateInvalidEmailError(it)
            }
        } else {
            _requestPasswordResult.onNext(transformer.transformErrorResult(throwable))
        }
    }
}