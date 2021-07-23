package com.tn07.survey

import com.tn07.survey.domain.entities.AccessToken
import com.tn07.survey.domain.entities.AnonymousToken
import com.tn07.survey.domain.entities.Token
import com.tn07.survey.domain.usecases.GetTokenUseCase
import io.reactivex.rxjava3.subjects.BehaviorSubject
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

/**
 * Created by toannguyen
 * Jul 23, 2021 at 21:07
 */
class MainViewModelTest {
    @Mock
    private lateinit var getTokenUseCase: GetTokenUseCase

    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = MainViewModel(getTokenUseCase)
    }

    @Test
    fun isLoggedIn_false() {
        Mockito.`when`(getTokenUseCase.getToken()).thenReturn(AnonymousToken)
        Assert.assertFalse(viewModel.isLoggedIn)
    }

    @Test
    fun isLoggedIn_true() {
        val accessToken = Mockito.mock(AccessToken::class.java)
        Mockito.`when`(getTokenUseCase.getToken()).thenReturn(accessToken)
        Assert.assertTrue(viewModel.isLoggedIn)
    }

    @Test
    fun getLoginStatusObservable() {
        val userSubject: BehaviorSubject<Token> = BehaviorSubject.createDefault(AnonymousToken)
        Mockito.`when`(getTokenUseCase.getTokenObservable()).thenReturn(userSubject)
        val testObserver = viewModel.loginStatusObservable.test()
            .assertValue(false)
            .assertNotComplete()

        //  first access token
        val firstToken = Mockito.mock(AccessToken::class.java)
        userSubject.onNext(firstToken)
        testObserver.assertValues(false, true)
            .assertNotComplete()

        //  second access token
        val secondToken = Mockito.mock(AccessToken::class.java)
        userSubject.onNext(secondToken)
        testObserver
            .assertValues(false, true)
            .assertNotComplete()
    }
}