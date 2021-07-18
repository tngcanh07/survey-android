package com.tn07.survey.data.survey

import com.tn07.survey.data.survey.model.SurveyResponse
import com.tn07.survey.domain.entities.Survey

/**
 * Created by toannguyen
 * Jul 18, 2021 at 09:20
 */
internal fun SurveyResponse.mapToSurveyEntity(): Survey {
    return object : Survey {
        override val id: String = this@mapToSurveyEntity.id
        override val title: String = attributes.title
        override val description: String = attributes.description
        override val coverImageUrl: String = attributes.coverImageUrl
    }
}