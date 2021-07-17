package com.tn07.survey.features.home.view

import androidx.recyclerview.widget.DiffUtil
import com.tn07.survey.features.home.uimodel.SurveyUiModel

/**
 * Created by toannguyen
 * Jul 17, 2021 at 22:55
 */
class SurveyDiffCallback : DiffUtil.ItemCallback<SurveyUiModel>() {
    override fun areItemsTheSame(oldItem: SurveyUiModel, newItem: SurveyUiModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: SurveyUiModel, newItem: SurveyUiModel): Boolean {
        return oldItem == newItem
    }
}