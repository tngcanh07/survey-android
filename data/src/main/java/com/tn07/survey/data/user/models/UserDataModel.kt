package com.tn07.survey.data.user.models

import com.google.gson.annotations.SerializedName
import com.tn07.survey.domain.entities.User

/**
 * Created by toannguyen
 * Jul 17, 2021 at 17:14
 */
data class UserDataModel(
    @SerializedName("email") override val email: String,
    @SerializedName("avatar_url") override val avatarUrl: String?
) : User