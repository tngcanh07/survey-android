package com.tn07.survey.data.survey.datasources.local

import com.tn07.survey.data.TestDataProvider
import com.tn07.survey.data.db.dao.SurveyDao
import com.tn07.survey.data.db.entity.SurveyEntity
import com.tn07.survey.domain.exceptions.DomainException
import io.reactivex.rxjava3.core.Maybe
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

/**
 * Created by toannguyen
 * Jul 25, 2021 at 11:11
 */
class SurveyLocalDataSourceImplTest {
    private lateinit var localDataSource: SurveyLocalDataSourceImpl

    @Captor
    private lateinit var surveyEntityListCaptor: ArgumentCaptor<List<SurveyEntity>>

    @Mock
    private lateinit var surveyDao: SurveyDao

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        localDataSource = SurveyLocalDataSourceImpl(surveyDao)
    }

    @Test
    fun saveSurveys() {
        val surveys = (1..5).map { TestDataProvider.generateSurvey() }

        localDataSource.saveSurveys(surveys)

        Mockito.verify(surveyDao).insertSurveys(surveyEntityListCaptor.capture() ?: emptyList())
        TestDataProvider.assertSurveys(surveys, surveyEntityListCaptor.value)

        Mockito.verifyNoMoreInteractions(surveyDao)
    }

    @Test
    fun clearSurveys() {
        localDataSource.clearSurveys()

        Mockito.verify(surveyDao).deleteAllSurveys()
        Mockito.verifyNoMoreInteractions(surveyDao)
    }

    @Test
    fun getAllSurveys() {
        val surveys = (1..5).map { TestDataProvider.generaSurveyEntity() }
        Mockito.`when`(surveyDao.getAllSurveys()).thenReturn(Maybe.just(surveys))

        localDataSource.getAllSurveys()
            .test()
            .assertValue(surveys)
    }

    @Test
    fun getAllSurveys_empty() {
        Mockito.`when`(surveyDao.getAllSurveys()).thenReturn(Maybe.empty())

        localDataSource.getAllSurveys()
            .test()
            .assertNoValues()
            .assertNoErrors()
            .assertComplete()
    }

    @Test
    fun getAllSurveys_error() {
        val expectedException = DomainException()
        Mockito.`when`(surveyDao.getAllSurveys()).thenReturn(Maybe.error(expectedException))

        localDataSource.getAllSurveys()
            .test()
            .assertError(expectedException)
    }
}