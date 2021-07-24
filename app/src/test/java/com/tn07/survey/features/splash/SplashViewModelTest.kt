package com.tn07.survey.features.splash

import com.tn07.survey.domain.entities.AccessToken
import com.tn07.survey.domain.entities.AnonymousToken
import com.tn07.survey.domain.usecases.GetTokenUseCase
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

/**
 * Created by toannguyen
 * Jul 24, 2021 at 15:45
 */
class SplashViewModelTest {

    @Mock
    private lateinit var getTokenUseCase: GetTokenUseCase

    private lateinit var viewModel: SplashViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = SplashViewModel(getTokenUseCase)
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
}