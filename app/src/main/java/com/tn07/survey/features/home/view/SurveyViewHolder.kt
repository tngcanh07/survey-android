package com.tn07.survey.features.home.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.Insets
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tn07.survey.R
import com.tn07.survey.databinding.ItemSurveyBinding
import com.tn07.survey.features.common.applySystemBarInsets
import com.tn07.survey.features.home.uimodel.SurveyUiModel

/**
 * Created by toannguyen
 * Jul 17, 2021 at 22:51
 */
class SurveyViewHolder(
    parent: ViewGroup,
    onOpenDetail: (SurveyUiModel) -> Unit
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_survey, parent, false)
) {
    private val binding: ItemSurveyBinding = ItemSurveyBinding.bind(itemView)

    private var uiModel: SurveyUiModel? = null

    init {
        binding.openDetailFab.setOnClickListener {
            uiModel?.let(onOpenDetail)
        }
    }

    fun bind(uiModel: SurveyUiModel, systemBarInsets: Insets?) {
        this.uiModel = null
        with(binding) {
            surveyTitle.text = uiModel.title
            surveyDescription.text = uiModel.description
            Glide.with(backgroundImage)
                .load(uiModel.backgroundImageUrl)
                .into(backgroundImage)

            systemBarInsets?.let {
                surveyContentBoundary.updateLayoutParams<ConstraintLayout.LayoutParams> {
                    applySystemBarInsets(it)
                }
            }
        }
        this.uiModel = uiModel
    }

}