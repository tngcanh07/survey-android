package com.tn07.survey.di.features

import com.tn07.survey.features.forgotpassword.transformer.ForgotPasswordTransformer
import com.tn07.survey.features.forgotpassword.transformer.ForgotPasswordTransformerImpl
import com.tn07.survey.features.forgotpassword.validator.ForgotPasswordFormValidator
import com.tn07.survey.features.forgotpassword.validator.ForgotPasswordFormValidatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/**
 * Created by toannguyen
 * Jul 25, 2021 at 15:24
 */
@InstallIn(ViewModelComponent::class)
@Module
interface ForgotPasswordModule {

    @Binds
    fun bindForgotPasswordTransformer(impl: ForgotPasswordTransformerImpl): ForgotPasswordTransformer

    @Binds
    fun bindForgotPasswordFormValidator(impl: ForgotPasswordFormValidatorImpl): ForgotPasswordFormValidator
}