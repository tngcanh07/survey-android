package com.tn07.survey.features.home.pagingsource

import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.tn07.survey.domain.exceptions.DomainException
import com.tn07.survey.features.home.uimodel.SurveyUiModel
import io.reactivex.rxjava3.core.Single

/**
 * Created by toannguyen
 * Jul 17, 2021 at 23:04
 */
class SurveyPagingSource(
    private val startPageIndex: Int
) : RxPagingSource<Int, SurveyUiModel>() {
    override fun getRefreshKey(state: PagingState<Int, SurveyUiModel>): Int? {
        return null
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, SurveyUiModel>> {
        val page = params.key ?: startPageIndex
        return try {
            val items = listOf(
                SurveyUiModel(
                    "id1",
                    "Survey title1",
                    "description",
                    "imageurl"
                ),
                SurveyUiModel(
                    "id2",
                    "Survey title2",
                    "description",
                    "imageurl"
                ),
                SurveyUiModel(
                    "id3",
                    "Survey title3",
                    "description",
                    "imageurl"
                ),
                SurveyUiModel(
                    "id4",
                    "Survey title4",
                    "description",
                    "imageurl"
                )
            )
            val result = LoadResult.Page(
                data = items,
                prevKey = if (page <= startPageIndex) null else page - 1,
                nextKey = if (items.size < params.loadSize) null else page + 1
            )
            Single.just(result)
        } catch (e: DomainException) {
            Single.just(LoadResult.Error(e))
        }
    }
}