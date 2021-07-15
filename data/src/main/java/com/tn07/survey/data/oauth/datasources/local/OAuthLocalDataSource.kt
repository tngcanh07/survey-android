package com.tn07.survey.data.oauth.datasources.local

import com.tn07.survey.data.oauth.models.AccessTokenDataModel

/**
 * Created by toannguyen
 * Jul 14, 2021 at 22:55
 */
interface OAuthLocalDataSource {
    fun storeAccessToken(accessToken: AccessTokenDataModel)

    fun getAccessToken(): AccessTokenDataModel?

    fun clearAccessToken()
}