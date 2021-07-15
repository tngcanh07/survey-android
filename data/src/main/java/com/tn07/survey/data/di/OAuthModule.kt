package com.tn07.survey.data.di

import com.tn07.survey.data.oauth.OAuthRepositoryImpl
import com.tn07.survey.data.oauth.datasources.local.OAuthLocalDataSource
import com.tn07.survey.data.oauth.datasources.local.OAuthLocalDataSourceImpl
import com.tn07.survey.data.oauth.datasources.remote.OAuthRemoteDataSource
import com.tn07.survey.data.oauth.datasources.remote.OAuthRemoteDataSourceImpl
import com.tn07.survey.domain.repositories.OAuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by toannguyen
 * Jul 14, 2021 at 23:58
 */
@Module
@InstallIn(SingletonComponent::class)
interface OAuthModule {
    @Binds
    @Singleton
    fun bindLocalDataSource(impl: OAuthLocalDataSourceImpl): OAuthLocalDataSource

    @Binds
    @Singleton
    fun bindRemoteDataSource(impl: OAuthRemoteDataSourceImpl): OAuthRemoteDataSource

    @Binds
    @Singleton
    fun bindRepository(impl: OAuthRepositoryImpl): OAuthRepository
}