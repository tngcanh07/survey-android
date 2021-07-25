package com.tn07.survey.features.forgotpassword

import com.tn07.survey.domain.usecases.ResetPasswordUseCase
import com.tn07.survey.features.base.BaseViewModel
import com.tn07.survey.features.common.SchedulerProvider
import com.tn07.survey.features.forgotpassword.transformer.ForgotPasswordTransformer
import com.tn07.survey.features.forgotpassword.uimodel.ForgotPasswordUiModel
import com.tn07.survey.features.forgotpassword.uimodel.InvalidEmailException
import com.tn07.survey.features.forgotpassword.uimodel.ResetPasswordResult
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
    private val resetPasswordUseCase: ResetPasswordUseCase,
    private val validator: ForgotPasswordFormValidator,
    private val transformer: ForgotPasswordTransformer,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel() {

    private val _forgotPasswordUiModel = BehaviorSubject.createDefault(transformer.initialUiModel)
    val forgotPasswordUiModel: Observable<ForgotPasswordUiModel>
        get() = _forgotPasswordUiModel.distinctUntilChanged()

    private val _resetPasswordResult = PublishSubject.create<ResetPasswordResult>()
    val resetPasswordResult: Observable<ResetPasswordResult>
        get() = _resetPasswordResult

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

    fun resetPassword() {
        Single.fromCallable {
            val uiModel = _forgotPasswordUiModel.value
            validator.validateResetPasswordForm(uiModel)
            return@fromCallable uiModel.email
        }
            .doOnSubscribe { onStartResetPassword() }
            .flatMapCompletable(resetPasswordUseCase::resetPassword)
            .subscribeOn(schedulerProvider.io())
            .subscribe(::onResetPasswordSuccess, ::onResetPasswordError)
            .addToCompositeDisposable()
    }

    private fun onStartResetPassword() {
        forgotPasswordUiModelEvents.onNext {
            transformer.updateLoadingStatus(it, true)
        }
    }

    private fun onResetPasswordSuccess() {
        forgotPasswordUiModelEvents.onNext {
            transformer.updateLoadingStatus(it, false)
        }
        _resetPasswordResult.onNext(ResetPasswordResult.Success)
    }

    private fun onResetPasswordError(throwable: Throwable) {
        forgotPasswordUiModelEvents.onNext {
            transformer.updateLoadingStatus(it, false)
        }
        if (throwable is InvalidEmailException) {
            forgotPasswordUiModelEvents.onNext {
                transformer.updateInvalidEmailError(it)
            }
        } else {
            _resetPasswordResult.onNext(transformer.transformErrorResult(throwable))
        }
    }
}