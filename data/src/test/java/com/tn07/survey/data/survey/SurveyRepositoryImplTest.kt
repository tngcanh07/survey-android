package com.tn07.survey.data.survey

import com.tn07.survey.data.TestDataProvider
import com.tn07.survey.data.db.entity.SurveyEntity
import com.tn07.survey.data.survey.datasources.local.SurveyLocalDataSource
import com.tn07.survey.data.survey.datasources.remote.SurveyRemoteDataSource
import com.tn07.survey.domain.entities.AccessToken
import com.tn07.survey.domain.entities.AnonymousToken
import com.tn07.survey.domain.entities.Token
import com.tn07.survey.domain.exceptions.ApiException
import com.tn07.survey.domain.repositories.OAuthRepository
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

/**
 * Created by toannguyen
 * Jul 23, 2021 at 13:59
 */
class SurveyRepositoryImplTest {
    private lateinit var repository: SurveyRepositoryImpl

    @Mock
    private lateinit var remoteDataSource: SurveyRemoteDataSource

    @Mock
    private lateinit var localDataSource: SurveyLocalDataSource

    @Mock
    private lateinit var oauthRepository: OAuthRepository
    private lateinit var tokenSubject: BehaviorSubject<Token>

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        tokenSubject = BehaviorSubject.createDefault(Mockito.mock(AccessToken::class.java))
        Mockito.`when`(oauthRepository.getTokenObservable()).thenReturn(tokenSubject)
        repository = SurveyRepositoryImpl(remoteDataSource, localDataSource, oauthRepository)
    }

    @Test
    fun clearSurveysWhenUserLoggedOut() {
        tokenSubject.onNext(AnonymousToken)
        Mockito.verify(localDataSource).clearSurveys()

        tokenSubject.onNext(Mockito.mock(AccessToken::class.java))
        Mockito.verifyNoMoreInteractions(localDataSource)
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

    @Test
    fun getLocalSurveys() {
        val data = (1..5).map { Mockito.mock(SurveyEntity::class.java) }
        Mockito.`when`(localDataSource.getAllSurveys())
            .thenReturn(Maybe.just(data))

        repository.getLocalSurveys()
            .test()
            .assertValue(data)
            .assertComplete()
    }

    @Test
    fun getLocalSurveys_error() {
        val expectedException = ApiException(456)
        Mockito.`when`(localDataSource.getAllSurveys())
            .thenReturn(Maybe.error(expectedException))

        repository.getLocalSurveys()
            .test()
            .assertError(expectedException)
    }
}
