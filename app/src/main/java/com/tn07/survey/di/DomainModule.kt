package com.tn07.survey.di

import com.tn07.survey.domain.usecases.GetTokenUseCase
import com.tn07.survey.domain.usecases.GetTokenUseCaseImpl
import com.tn07.survey.domain.usecases.LoginUseCase
import com.tn07.survey.domain.usecases.LoginUseCaseImpl
import com.tn07.survey.domain.usecases.LogoutUseCase
import com.tn07.survey.domain.usecases.LogoutUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/**
 * Created by toannguyen
 * Jul 16, 2021 at 13:56
 */
@InstallIn(ViewModelComponent::class)
@Module
interface DomainModule {
    @Binds
    fun bindLoginUseCase(useCase: LoginUseCaseImpl): LoginUseCase

    @Binds
    fun bindGetTokenUseCase(useCase: GetTokenUseCaseImpl): GetTokenUseCase

    @Binds
    fun bindLogoutUseCase(useCase: LogoutUseCaseImpl): LogoutUseCase
}