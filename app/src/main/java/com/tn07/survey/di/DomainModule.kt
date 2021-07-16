package com.tn07.survey.di

import com.tn07.survey.domain.usecases.GetUserUseCase
import com.tn07.survey.domain.usecases.GetUserUseCaseImpl
import com.tn07.survey.domain.usecases.LoginUseCase
import com.tn07.survey.domain.usecases.LoginUseCaseImpl
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
    fun bindGetTokenUseCase(useCase: GetUserUseCaseImpl): GetUserUseCase
}