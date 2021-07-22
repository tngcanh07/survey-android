package com.tn07.survey.domain.usecases

import com.tn07.survey.domain.entities.User
import com.tn07.survey.domain.repositories.UserRepository
import io.reactivex.rxjava3.subjects.BehaviorSubject
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito

/**
 * Created by toannguyen
 * Jul 22, 2021 at 19:02
 */
internal class GetUserUseCaseImplTest {
    private lateinit var useCase: GetUserUseCaseImpl
    private lateinit var repository: UserRepository

    @BeforeEach
    fun setUp() {
        repository = Mockito.mock(UserRepository::class.java)
        useCase = GetUserUseCaseImpl(repository)
    }

    @Test
    fun getUserObservable() {
        val user1 = Mockito.mock(User::class.java)
        val user2 = Mockito.mock(User::class.java)
        val userSubject = BehaviorSubject.createDefault(user1)

        Mockito.`when`(repository.getUserObservable()).thenReturn(userSubject)

        // first item
        val subscriber = useCase.getUserObservable()
            .test()
        subscriber.assertValueCount(1)
            .assertValue(user1)
            .assertNotComplete()
            .assertNoErrors()

        // second item
        userSubject.onNext(user2)
        subscriber.assertValueCount(2)
            .assertValues(user1, user2)
            .assertNotComplete()
            .assertNoErrors()
    }
}