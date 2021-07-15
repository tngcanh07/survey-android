package com.tn07.survey.data.oauth.datasources.remote.responsemodel

import com.google.gson.annotations.SerializedName
import com.tn07.survey.data.oauth.models.AccessTokenDataModel

/**
 * Created by toannguyen
 * Jul 14, 2021 at 23:04
 */
class AccessTokenResponse(
    @SerializedName("id")
    val id: Int,

    @SerializedName("type")
    val type: String,

    @SerializedName("attributes")
    val accessToken: AccessTokenDataModel
)
