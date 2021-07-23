package com.tn07.survey.features.home

import com.tn07.survey.domain.entities.Pageable
import com.tn07.survey.domain.entities.Survey
import com.tn07.survey.domain.entities.User
import com.tn07.survey.domain.exceptions.ConnectionException
import com.tn07.survey.domain.usecases.GetSurveyUseCase
import com.tn07.survey.domain.usecases.GetUserUseCase
import com.tn07.survey.domain.usecases.LogoutUseCase
import com.tn07.survey.features.TestComponent
import com.tn07.survey.features.home.transformer.HomeTransformer
import com.tn07.survey.features.home.uimodel.LogoutResultUiModel
import com.tn07.survey.features.home.uimodel.SurveyUiModel
import com.tn07.survey.features.home.uimodel.UserUiModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.net.SocketTimeoutException
import kotlin.math.min

/**
 * Created by toannguyen
 * Jul 23, 2021 at 19:20
 */
private const val PAGE_SIZE = 5

class HomeViewModelTest {
    private lateinit var viewModel: HomeViewModel

    @Mock
    private lateinit var getUserUseCase: GetUserUseCase

    @Mock
    private lateinit var logoutUseCase: LogoutUseCase

    @Mock
    private lateinit var getSurveyUseCase: GetSurveyUseCase

    @Mock
    private lateinit var transformer: HomeTransformer

    @Mock
    private lateinit var initialUser: User

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Mockito.`when`(getUserUseCase.getUserObservable())
            .thenReturn(Observable.just(initialUser))

        viewModel = HomeViewModel(
            getUserUseCase,
            logoutUseCase,
            getSurveyUseCase,
            transformer,
            TestComponent.schedulerProvider
        )
    }

    @Test
    fun initUser() {
        // user
        val user = Mockito.mock(User::class.java)
        val userUiModel = Mockito.mock(UserUiModel::class.java)
        Mockito.`when`(getUserUseCase.getUserObservable()).thenReturn(Observable.just(user))
        Mockito.`when`(transformer.transformUser(user)).thenReturn(userUiModel)

        viewModel.initUser()

        viewModel.user
            .test()
            .assertValue(userUiModel)
            .assertNotComplete()
    }

    @Test
    fun logOut() {
        Mockito.`when`(logoutUseCase.logout()).thenReturn(Completable.complete())

        viewModel.logout()
            .test()
            .assertValue(LogoutResultUiModel.Success)
            .assertComplete()
            .assertNoErrors()
    }

    @Test
    fun logOut_error() {
        val exception = ConnectionException(SocketTimeoutException())
        Mockito.`when`(logoutUseCase.logout()).thenReturn(Completable.error(exception))

        viewModel.logout()
            .test()
            .assertValue(LogoutResultUiModel.Error)
            .assertComplete()
            .assertNoErrors()
    }

    @Test
    fun initSurvey() {
        val surveys = mockSurveyData(1, 20)

        viewModel.initSurveys()

        viewModel.surveyListResult
            .test()
            .assertValues(surveys)
            .assertNotComplete()
    }

    @Test
    fun loadSurveys() {
        val total = 7
        val firstPageSurveys = mockSurveyData(1, total)
        val secondPageSurveys = mockSurveyData(2, total)

        // first pages
        viewModel.setCurrentPage(0)
        viewModel.surveyListResult
            .test()
            .awaitCount(2)
            .assertValue(firstPageSurveys)
            .assertNotComplete()

        // still far from the end => don't load next page
        viewModel.setCurrentPage(1)

        // second page
        viewModel.setCurrentPage(firstPageSurveys.lastIndex)
        val expected2PagesSurveys = firstPageSurveys + secondPageSurveys
        viewModel.surveyListResult
            .test()
            .awaitCount(3)
            .assertValue(expected2PagesSurveys)
            .assertNotComplete()

        // no more data
        viewModel.setCurrentPage(expected2PagesSurveys.lastIndex)
    }

    @Test
    fun refreshSurveys() {
        mockSurveyData(1, 20)
        viewModel.initSurveys()

        val refreshedSurveys = mockSurveyData(1, 20)
        viewModel.refreshSurveys()
        viewModel.surveyListResult
            .test()
            .assertValues(refreshedSurveys)
            .assertNotComplete()
    }

    private fun mockSurveyData(
        page: Int,
        total: Int
    ): List<SurveyUiModel> {
        Assert.assertTrue(page > 0)
        val pages = (total - 1) / PAGE_SIZE + 1
        val count = min(total, page * PAGE_SIZE) - ((page - 1) * PAGE_SIZE)
        val data = object : Pageable<Survey> {
            override val items: List<Survey> = (0 until count).map {
                Mockito.mock(Survey::class.java)
            }
            override val page: Int = page
            override val pageSize: Int = PAGE_SIZE
            override val pages: Int = pages
            override val total: Int = total
        }

        Mockito.`when`(getSurveyUseCase.getSurveys(page, PAGE_SIZE))
            .thenReturn(Single.just(data))
        return data.items.map {
            val uiModel = Mockito.mock(SurveyUiModel::class.java)
            Mockito.`when`(transformer.transformSurvey(it)).thenReturn(uiModel)
            uiModel
        }
    }
}

