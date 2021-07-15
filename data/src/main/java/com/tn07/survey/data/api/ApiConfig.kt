package com.tn07.survey.data.api

/**
 * Created by toannguyen
 * Jul 14, 2021 at 23:25
 */
data class ApiConfig(
    val baseUrl: String,
    val isLoggingEnabled: Boolean
)

data class OAuthConfig(
    val baseUrl: String,
    val clientId: String,
    val clientSecret: String,
    val isLoggingEnabled: Boolean
)