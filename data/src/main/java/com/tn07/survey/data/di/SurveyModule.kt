package com.tn07.survey.data.di

import com.tn07.survey.data.survey.SurveyRepositoryImpl
import com.tn07.survey.data.survey.datasources.local.SurveyLocalDataSource
import com.tn07.survey.data.survey.datasources.local.SurveyLocalDataSourceImpl
import com.tn07.survey.data.survey.datasources.remote.SurveyRemoteDataSource
import com.tn07.survey.data.survey.datasources.remote.SurveyRemoteDataSourceImpl
import com.tn07.survey.domain.repositories.SurveyRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Created by toannguyen
 * Jul 18, 2021 at 09:39
 */
@InstallIn(SingletonComponent::class)
@Module
interface SurveyModule {
    @Binds
    fun bindSurveyRemoteDataSource(impl: SurveyRemoteDataSourceImpl): SurveyRemoteDataSource

    @Binds
    fun bindSurveyLocalDataSource(impl: SurveyLocalDataSourceImpl): SurveyLocalDataSource

    @Binds
    fun bindSurveyRepository(impl: SurveyRepositoryImpl): SurveyRepository
}