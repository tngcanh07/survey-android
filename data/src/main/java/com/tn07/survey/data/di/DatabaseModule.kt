package com.tn07.survey.data.di

import android.app.Application
import com.tn07.survey.data.db.SurveyDatabase
import com.tn07.survey.data.db.createSurveyDatabase
import com.tn07.survey.data.db.dao.SurveyDao
import com.tn07.survey.data.di.qualifier.SurveyDatabaseQualifier
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by toannguyen
 * Jul 24, 2021 at 23:18
 */
@Module
@InstallIn(SingletonComponent::class)
internal class DatabaseModule {

    @Provides
    @Singleton
    @SurveyDatabaseQualifier
    fun provideSurveyDatabase(application: Application): SurveyDatabase {
        return createSurveyDatabase(application)
    }

    @Provides
    @Singleton
    @SurveyDatabaseQualifier
    fun provideSurveyDao(@SurveyDatabaseQualifier surveyDatabase: SurveyDatabase): SurveyDao {
        return surveyDatabase.surveyDao()
    }
}