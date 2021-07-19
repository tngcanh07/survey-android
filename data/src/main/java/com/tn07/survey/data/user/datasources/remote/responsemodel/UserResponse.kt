package com.tn07.survey.data.user.datasources.remote.responsemodel

import com.google.gson.annotations.SerializedName
import com.tn07.survey.data.user.models.UserDataModel

/**
 * Created by toannguyen
 * Jul 17, 2021 at 17:17
 */
class UserResponse(
    @SerializedName("id")
    val id: Int,

    @SerializedName("type")
    val type: String,

    @SerializedName("attributes")
    val user: UserDataModel
)