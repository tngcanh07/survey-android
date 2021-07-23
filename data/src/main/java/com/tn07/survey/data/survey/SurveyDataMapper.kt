package com.tn07.survey.data.survey

import com.tn07.survey.data.api.PageableApiResponse
import com.tn07.survey.data.survey.model.SurveyResponse
import com.tn07.survey.domain.entities.Pageable
import com.tn07.survey.domain.entities.Survey

/**
 * Created by toannguyen
 * Jul 18, 2021 at 09:20
 */

internal fun PageableApiResponse<SurveyResponse>.mapToPageableEntity(): Pageable<Survey> {
    return object : Pageable<Survey> {
        override val items: List<Survey> = data.map(SurveyResponse::mapToSurveyEntity)
        override val page: Int = meta.page
        override val pages: Int = meta.pages
        override val pageSize: Int = meta.pageSize
        override val total: Int = meta.records
    }
}

internal val SurveyResponse.highQualityCoverImageUrl
    get() = "${attributes.coverImageUrl}l"

internal fun SurveyResponse.mapToSurveyEntity(): Survey {
    return object : Survey {
        override val id: String = this@mapToSurveyEntity.id
        override val title: String = attributes.title
        override val description: String = attributes.description
        override val coverImageUrl: String = highQualityCoverImageUrl
    }
}

