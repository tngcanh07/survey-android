package com.tn07.survey.data.survey.model

import com.google.gson.annotations.SerializedName

/**
 * Created by toannguyen
 * Jul 18, 2021 at 09:01
 */
data class SurveyResponse(
    @SerializedName("id")
    val id: String,

    @SerializedName("type")
    val type: String,

    @SerializedName("attributes")
    val attributes: SurveyAttribute
)
