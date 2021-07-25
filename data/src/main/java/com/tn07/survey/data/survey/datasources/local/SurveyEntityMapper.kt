package com.tn07.survey.data.survey.datasources.local

import com.tn07.survey.data.db.entity.SurveyEntity
import com.tn07.survey.domain.entities.Survey

/**
 * Created by toannguyen
 * Jul 24, 2021 at 23:30
 */

fun Survey.toSurveyEntity(): SurveyEntity {
    return this as? SurveyEntity
        ?: SurveyEntity(
            id = id,
            title = title,
            description = description,
            coverImageUrl = coverImageUrl
        )
}

fun SurveyEntity.toSurvey(): Survey {
    return this
}