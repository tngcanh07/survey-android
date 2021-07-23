package com.tn07.survey.data.user

import com.tn07.survey.data.TestDataProvider
import com.tn07.survey.data.user.datasources.remote.UserRemoteDataSource
import com.tn07.survey.domain.exceptions.ApiException
import io.reactivex.rxjava3.core.Single
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

/**
 * Created by toannguyen
 * Jul 23, 2021 at 14:36
 */
class UserRepositoryImplTest {
    private lateinit var repository: UserRepositoryImpl
    private lateinit var remoteDataSource: UserRemoteDataSource

    @Before
    fun setUp() {
        remoteDataSource = Mockito.mock(UserRemoteDataSource::class.java)
        repository = UserRepositoryImpl(remoteDataSource)
    }

    @Test
    fun getUser() {
        val userModel = TestDataProvider.userModel

        Mockito.`when`(remoteDataSource.getUser())
            .thenReturn(Single.just(userModel))

        repository.getUserObservable()
            .test()
            .await()
            .assertNoErrors()
            .assertValue {
                Assert.assertEquals(userModel.avatarUrl, it.avatarUrl)
                Assert.assertEquals(userModel.email, it.email)
                return@assertValue true
            }
    }

    @Test
    fun getSurveys_error() {
        val expectedException = ApiException(456)

        Mockito.`when`(remoteDataSource.getUser())
            .thenReturn(Single.error(expectedException))

        repository.getUserObservable()
            .test()
            .await()
            .assertError(expectedException)

    }
}