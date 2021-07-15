package com.tn07.survey.di

import com.tn07.survey.BuildConfig
import com.tn07.survey.data.api.ApiConfig
import com.tn07.survey.data.api.OAuthConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by toannguyen
 * Jul 15, 2021 at 09:21
 */
@Module
@InstallIn(SingletonComponent::class)
class AppConfigModule {

    @Provides
    @Singleton
    fun providesOAuthConfig(): OAuthConfig {
        return OAuthConfig(
            baseUrl = BuildConfig.BASE_URL,
            clientId = BuildConfig.CLIENT_ID,
            clientSecret = BuildConfig.CLIENT_SECRET,
            isLoggingEnabled = BuildConfig.DEBUG
        )
    }

    @Provides
    @Singleton
    fun providesApiConfig(): ApiConfig {
        return ApiConfig(
            baseUrl = BuildConfig.BASE_URL,
            isLoggingEnabled = BuildConfig.DEBUG
        )
    }
}