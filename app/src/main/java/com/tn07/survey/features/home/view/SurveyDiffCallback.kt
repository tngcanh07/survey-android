package com.tn07.survey.features.home.view

import androidx.recyclerview.widget.DiffUtil
import com.tn07.survey.features.home.uimodel.SurveyUiModel

/**
 * Created by toannguyen
 * Jul 17, 2021 at 22:55
 */
class SurveyDiffCallback(
    private val oldList: List<SurveyUiModel>,
    private val newList: List<SurveyUiModel>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }
}