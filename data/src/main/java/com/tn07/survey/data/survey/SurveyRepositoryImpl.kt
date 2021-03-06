package com.tn07.survey.data.survey

import com.tn07.survey.data.api.PageableApiResponse
import com.tn07.survey.data.survey.datasources.local.SurveyLocalDataSource
import com.tn07.survey.data.survey.datasources.remote.SurveyRemoteDataSource
import com.tn07.survey.data.survey.model.SurveyResponse
import com.tn07.survey.domain.entities.AnonymousToken
import com.tn07.survey.domain.entities.Pageable
import com.tn07.survey.domain.entities.Survey
import com.tn07.survey.domain.repositories.OAuthRepository
import com.tn07.survey.domain.repositories.SurveyRepository
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import javax.inject.Inject

/**
 * Created by toannguyen
 * Jul 18, 2021 at 08:56
 */

const val SURVEY_FIRST_PAGE_INDEX = 1

class SurveyRepositoryImpl @Inject constructor(
    private val remoteDataSource: SurveyRemoteDataSource,
    private val localDataSource: SurveyLocalDataSource,
    oauthRepository: OAuthRepository
) : SurveyRepository {

    var disposable: Disposable? = null

    init {
        disposable = oauthRepository.getTokenObservable()
            .skip(1)
            .filter { it is AnonymousToken }
            .subscribe {
                localDataSource.clearSurveys()
            }
    }

    override fun getSurveys(page: Int, pageSize: Int): Single<Pageable<Survey>> {
        return remoteDataSource.getSurveys(pageNumber = page, pageSize = pageSize)
            .map(PageableApiResponse<SurveyResponse>::mapToPageableEntity)
            .map {
                if (page == SURVEY_FIRST_PAGE_INDEX) {
                    localDataSource.clearSurveys()
                }
                localDataSource.saveSurveys(it.items)
                it
            }
    }

    override fun getLocalSurveys(): Maybe<List<Survey>> {
        return localDataSource.getAllSurveys()
    }
}