package com.tn07.survey.data.di

import com.tn07.survey.data.oauth.OAuthRepositoryImpl
import com.tn07.survey.data.oauth.datasources.local.OAuthLocalDataSource
import com.tn07.survey.data.oauth.datasources.local.OAuthLocalDataSourceImpl
import com.tn07.survey.data.oauth.datasources.remote.OAuthRemoteDataSource
import com.tn07.survey.data.oauth.datasources.remote.OAuthRemoteDataSourceImpl
import com.tn07.survey.domain.repositories.OAuthRepository
import dagger.Binds

/**
 * Created by toannguyen
 * Jul 14, 2021 at 23:58
 */
interface OAuthModule {
    @Binds
    fun bindLocalDataSource(impl: OAuthLocalDataSourceImpl): OAuthLocalDataSource

    @Binds
    fun bindRemoteDataSource(impl: OAuthRemoteDataSourceImpl): OAuthRemoteDataSource

    @Binds
    fun bindRepository(impl: OAuthRepositoryImpl): OAuthRepository
}