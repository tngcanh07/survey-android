package com.tn07.survey.features.home.pagingsource

import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.tn07.survey.domain.entities.Survey
import com.tn07.survey.domain.usecases.GetSurveyUseCase
import io.reactivex.rxjava3.core.Single

/**
 * Created by toannguyen
 * Jul 17, 2021 at 23:04
 */
class SurveyPagingSource(
    private val getSurveyUseCase: GetSurveyUseCase,
    private val startPageIndex: Int
) : RxPagingSource<Int, Survey>() {
    override fun getRefreshKey(state: PagingState<Int, Survey>): Int? {
        return null
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Survey>> {
        val pageNumber = params.key ?: startPageIndex
        val pageSize = params.loadSize
        
        return getSurveyUseCase.getSurveys(page = pageNumber, pageSize = pageSize)
            .map<LoadResult<Int, Survey>> {
                LoadResult.Page(
                    data = it,
                    prevKey = if (pageNumber <= startPageIndex) null else pageNumber - 1,
                    nextKey = if (it.size < pageSize) null else pageNumber + 1
                )
            }
            .onErrorResumeNext {
                Single.just(LoadResult.Error(it))
            }
    }
}