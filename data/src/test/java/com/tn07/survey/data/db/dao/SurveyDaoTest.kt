package com.tn07.survey.data.db.dao

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.tn07.survey.data.TestDataProvider
import com.tn07.survey.data.db.SurveyDatabase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * Created by toannguyen
 * Jul 25, 2021 at 11:45
 */
@RunWith(RobolectricTestRunner::class)
class SurveyDaoTest {

    private lateinit var surveyDao: SurveyDao

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val surveyDatabase =
            Room.databaseBuilder(context, SurveyDatabase::class.java, "survey.test.db")
                .allowMainThreadQueries()
                .build()

        surveyDao = surveyDatabase.surveyDao()
    }

    @Test
    fun `test insert, query and delete survey data`() {
        val surveys = (1..5).map { TestDataProvider.generaSurveyEntity() }

        // init state, no survey
        surveyDao.getAllSurveys()
            .test()
            .assertValue(emptyList())
            .assertNoErrors()
            .assertComplete()

        // insert surveys
        surveyDao.insertSurveys(surveys)
        surveyDao.getAllSurveys()
            .test()
            .assertValue {
                TestDataProvider.assertSurveys(surveys, it)
                return@assertValue true
            }
            .assertComplete()

        // delete all surveys
        surveyDao.deleteAllSurveys()
        surveyDao.getAllSurveys()
            .test()
            .assertValue(emptyList())
            .assertNoErrors()
            .assertComplete()
    }
}