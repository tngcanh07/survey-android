package com.tn07.survey.data.survey

import com.tn07.survey.data.TestDataProvider
import com.tn07.survey.data.survey.datasources.remote.SurveyRemoteDataSource
import com.tn07.survey.domain.exceptions.ApiException
import io.reactivex.rxjava3.core.Single
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

/**
 * Created by toannguyen
 * Jul 23, 2021 at 13:59
 */
class SurveyRepositoryImplTest {
    private lateinit var repository: SurveyRepositoryImpl
    private lateinit var remoteDataSource: SurveyRemoteDataSource

    @Before
    fun setUp() {
        remoteDataSource = Mockito.mock(SurveyRemoteDataSource::class.java)
        repository = SurveyRepositoryImpl(remoteDataSource)
    }

    @Test
    fun getSurveys() {
        val page = 1
        val pageSize = 2
        val surveysResponse = TestDataProvider.surveysResponse

        Mockito.`when`(remoteDataSource.getSurveys(page, pageSize))
            .thenReturn(Single.just(surveysResponse))

        repository.getSurveys(page, pageSize)
            .test()
            .await()
            .assertNoErrors()
            .assertValue {
                // metadata
                Assert.assertEquals(surveysResponse.meta.page, it.page)
                Assert.assertEquals(surveysResponse.meta.pageSize, it.pageSize)
                Assert.assertEquals(surveysResponse.meta.pages, it.pages)
                Assert.assertEquals(surveysResponse.meta.records, it.total)

                // data
                Assert.assertEquals(2, it.items.size)
                it.items.forEachIndexed { index, survey ->
                    val expectedItem = surveysResponse.data[index]
                    val bigCoverImage = expectedItem.attributes.coverImageUrl.plus("l")
                    Assert.assertEquals(expectedItem.id, survey.id)
                    Assert.assertEquals(expectedItem.attributes.title, survey.title)
                    Assert.assertEquals(expectedItem.attributes.description, survey.description)
                    Assert.assertEquals(bigCoverImage, survey.coverImageUrl)
                }
                return@assertValue true
            }
    }

    @Test
    fun getSurveys_error() {
        val page = 1
        val pageSize = 2
        val expectedException = ApiException(456)

        Mockito.`when`(remoteDataSource.getSurveys(page, pageSize))
            .thenReturn(Single.error(expectedException))

        repository.getSurveys(page, pageSize)
            .test()
            .await()
            .assertError(expectedException)

    }
}