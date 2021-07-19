package com.tn07.survey.data.survey

import com.tn07.survey.data.api.PageableApiResponse
import com.tn07.survey.data.survey.datasources.remote.SurveyRemoteDataSource
import com.tn07.survey.data.survey.model.SurveyResponse
import com.tn07.survey.domain.entities.Pageable
import com.tn07.survey.domain.entities.Survey
import com.tn07.survey.domain.repositories.SurveyRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

/**
 * Created by toannguyen
 * Jul 18, 2021 at 08:56
 */
class SurveyRepositoryImpl @Inject constructor(
    private val remoteDataSource: SurveyRemoteDataSource
) : SurveyRepository {

    override fun getSurveys(page: Int, pageSize: Int): Single<Pageable<Survey>> {
        return remoteDataSource.getSurveys(pageNumber = page, pageSize = pageSize)
            .map(PageableApiResponse<SurveyResponse>::mapToPageableEntity)
    }
}