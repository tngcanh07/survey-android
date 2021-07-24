package com.tn07.survey.domain.usecases

import com.tn07.survey.domain.entities.Pageable
import com.tn07.survey.domain.entities.Survey
import com.tn07.survey.domain.repositories.SurveyRepository
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

/**
 * Created by toannguyen
 * Jul 18, 2021 at 08:47
 */
class GetSurveyUseCaseImpl @Inject constructor(
    private val repository: SurveyRepository
) : GetSurveyUseCase {

    override fun getSurveys(
        page: Int,
        pageSize: Int
    ): Single<Pageable<Survey>> {
        return repository.getSurveys(
            page = page,
            pageSize = pageSize
        )
    }

    override fun getLocalSurveys(): Maybe<List<Survey>> {
        return repository.getLocalSurveys()
    }
}