package com.tn07.survey.di

import com.tn07.survey.features.common.SchedulerProvider
import com.tn07.survey.features.common.SchedulerProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by toannguyen
 * Jul 19, 2021 at 12:04
 */
@InstallIn(SingletonComponent::class)
@Module
interface AppModule {
    @Binds
    @Singleton
    fun bindRxSchedulerProvider(impl: SchedulerProviderImpl): SchedulerProvider
}