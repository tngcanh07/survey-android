package com.tn07.survey.features.home.view

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.tn07.survey.features.home.uimodel.SurveyUiModel

/**
 * Created by toannguyen
 * Jul 17, 2021 at 22:34
 */
class SurveyAdapter() : PagingDataAdapter<SurveyUiModel, SurveyViewHolder>(SurveyDiffCallback()) {
    override fun onBindViewHolder(holder: SurveyViewHolder, position: Int) {
        getItem(position)?.let(holder::bind)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurveyViewHolder {
        return SurveyViewHolder(parent)
    }
}