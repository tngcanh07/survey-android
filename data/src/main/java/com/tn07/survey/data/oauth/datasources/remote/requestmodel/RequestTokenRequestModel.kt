package com.tn07.survey.data.oauth.datasources.remote.requestmodel

import com.google.gson.annotations.SerializedName
import com.tn07.survey.data.api.OAUTH_GRANT_TYPE_PASSWORD

/**
 * Created by toannguyen
 * Jul 14, 2021 at 23:13
 */
class RequestTokenRequestModel(
    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("client_id")
    val clientId: String,

    @SerializedName("client_secret")
    val clientSecret: String
) {

    @SerializedName("grant_type")
    val grantType: String = OAUTH_GRANT_TYPE_PASSWORD
}
