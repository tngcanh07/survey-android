package com.tn07.survey.data.survey.model

import com.google.gson.annotations.SerializedName

/**
 * Created by toannguyen
 * Jul 18, 2021 at 09:02
 */
class SurveyAttribute(
    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("cover_image_url")
    val coverImageUrl: String
)


