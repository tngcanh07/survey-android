package com.tn07.survey.data.survey.model

import com.google.gson.annotations.SerializedName
import java.util.Date

/**
 * Created by toannguyen
 * Jul 18, 2021 at 09:02
 */
class SurveyAttribute(
    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("thank_email_above_threshold")
    val thankEmailAboveThreshold: String,

    @SerializedName("thank_email_below_threshold")
    val thankEmailBelowThreshold: String,

    @SerializedName("is_active")
    val isActive: Boolean,

    @SerializedName("cover_image_url")
    val coverImageUrl: String,

    @SerializedName("created_at")
    val createdAt: Date,

    @SerializedName("active_at")
    val activeAt: Date?,

    @SerializedName("inactive_at")
    val inactiveAt: Date?,

    @SerializedName("survey_type")
    val surveyType: String,
)


