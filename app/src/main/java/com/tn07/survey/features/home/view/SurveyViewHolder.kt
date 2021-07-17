package com.tn07.survey.features.home.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tn07.survey.R
import com.tn07.survey.databinding.ItemSurveyBinding
import com.tn07.survey.features.home.uimodel.SurveyUiModel

/**
 * Created by toannguyen
 * Jul 17, 2021 at 22:51
 */
class SurveyViewHolder(
    parent: ViewGroup
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_survey, parent, false)
) {
    private val binding = ItemSurveyBinding.bind(itemView)

    private var uiModel: SurveyUiModel? = null

    fun bind(uiModel: SurveyUiModel) {
        with(binding) {
            surveyTitle.text = uiModel.title
            surveyDescription.text = uiModel.description
        }
    }
}