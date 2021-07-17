package com.tn07.survey.data.di

import com.tn07.survey.data.user.UserRepositoryImpl
import com.tn07.survey.data.user.datasources.remote.UserRemoteDataSource
import com.tn07.survey.data.user.datasources.remote.UserRemoteDataSourceImpl
import com.tn07.survey.domain.repositories.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Created by toannguyen
 * Jul 17, 2021 at 17:29
 */
@InstallIn(SingletonComponent::class)
@Module
interface UserModule {
    @Binds
    fun bindUserRemoteDataSource(impl: UserRemoteDataSourceImpl): UserRemoteDataSource

    @Binds
    fun bindUserRepository(impl: UserRepositoryImpl): UserRepository
}