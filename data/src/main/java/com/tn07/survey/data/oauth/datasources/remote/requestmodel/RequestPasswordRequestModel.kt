package com.tn07.survey.data.oauth.datasources.remote.requestmodel

import com.google.gson.annotations.SerializedName

/**
 * Created by toannguyen
 * Jul 14, 2021 at 23:15
 */
class RequestPasswordRequestModel(
    @SerializedName("user")
    val user: User,

    @SerializedName("client_id")
    val clientId: String,

    @SerializedName("client_secret")
    val clientSecret: String
) {

    data class User(
        @SerializedName("email")
        val email: String
    )
}

