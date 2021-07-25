package com.tn07.survey.domain.usecases

import com.tn07.survey.domain.entities.Pageable
import com.tn07.survey.domain.entities.Survey
import com.tn07.survey.domain.exceptions.DomainException
import com.tn07.survey.domain.repositories.SurveyRepository
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito

/**
 * Created by toannguyen
 * Jul 22, 2021 at 18:52
 */
internal class GetSurveyUseCaseImplTest {
    private lateinit var useCase: GetSurveyUseCaseImpl
    private lateinit var repository: SurveyRepository

    @BeforeEach
    fun setUp() {
        repository = Mockito.mock(SurveyRepository::class.java)
        useCase = GetSurveyUseCaseImpl(repository)
    }

    @Test
    fun getSurveys_success() {
        val page = 12
        val pageSize = 5
        val expectedResult = Mockito.mock(SurveyPageable::class.java)

        Mockito.`when`(repository.getSurveys(page = page, pageSize = pageSize))
            .thenReturn(Single.just(expectedResult))

        useCase.getSurveys(page = page, pageSize = pageSize)
            .test()
            .assertValue(expectedResult)
            .assertComplete()
            .assertNoErrors()

        Mockito.verify(repository).getSurveys(page = page, pageSize = pageSize)
    }

    @Test
    fun getSurveys_error() {
        val page = 12
        val pageSize = 5
        val expectedException = DomainException()

        Mockito.`when`(repository.getSurveys(page = page, pageSize = pageSize))
            .thenReturn(Single.error(expectedException))


        useCase.getSurveys(page = page, pageSize = pageSize)
            .test()
            .assertError(expectedException)

        Mockito.verify(repository).getSurveys(page = page, pageSize = pageSize)
    }

    @Test
    fun getLocalSurveys() {
        val surveys = listOf(
            Mockito.mock(Survey::class.java),
            Mockito.mock(Survey::class.java),
            Mockito.mock(Survey::class.java)
        )
        Mockito.`when`(repository.getLocalSurveys())
            .thenReturn(Maybe.just(surveys))

        useCase.getLocalSurveys()
            .test()
            .assertNoErrors()
            .assertComplete()
            .assertValue(surveys)

        Mockito.verify(repository).getLocalSurveys()
    }


    @Test
    fun getLocalSurveys_error() {
        val expectedException = DomainException()

        Mockito.`when`(repository.getLocalSurveys())
            .thenReturn(Maybe.error(expectedException))


        useCase.getLocalSurveys()
            .test()
            .assertNoValues()
            .assertError(expectedException)

        Mockito.verify(repository).getLocalSurveys()
    }
}

private interface SurveyPageable : Pageable<Survey>