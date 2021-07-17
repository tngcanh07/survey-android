package com.tn07.survey.di.features

import com.tn07.survey.features.login.transformer.LoginTransformer
import com.tn07.survey.features.login.transformer.LoginTransformerImpl
import com.tn07.survey.features.login.validator.LogInFormValidator
import com.tn07.survey.features.login.validator.LogInFormValidatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/**
 * Created by toannguyen
 * Jul 17, 2021 at 08:55
 */
@InstallIn(ViewModelComponent::class)
@Module
interface LoginModule {
    @Binds
    fun bindLoginTransformer(impl: LoginTransformerImpl): LoginTransformer

    @Binds
    fun bindLoginFormValidator(impl: LogInFormValidatorImpl): LogInFormValidator
}