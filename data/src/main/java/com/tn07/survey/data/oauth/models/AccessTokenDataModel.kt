package com.tn07.survey.data.oauth.models

import com.google.gson.annotations.SerializedName
import com.tn07.survey.domain.entities.AccessToken

/**
 * Created by toannguyen
 * Jul 14, 2021 at 23:04
 */
data class AccessTokenDataModel(
    @SerializedName("access_token")
    override val accessToken: String,

    @SerializedName("refresh_token")
    override val refreshToken: String,

    @SerializedName("token_type")
    override val tokenType: String,

    @SerializedName("expires_in")
    override val expiresIn: Long,

    @SerializedName("created_at")
    override val createdAt: Long
) : AccessToken