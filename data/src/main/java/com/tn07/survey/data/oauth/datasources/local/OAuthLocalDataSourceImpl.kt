package com.tn07.survey.data.oauth.datasources.local

import com.tn07.survey.data.oauth.models.AccessTokenDataModel
import javax.inject.Inject

/**
 * Created by toannguyen
 * Jul 14, 2021 at 23:08
 */
class OAuthLocalDataSourceImpl @Inject constructor() : OAuthLocalDataSource {

    private var accessToken: AccessTokenDataModel? = null

    override fun storeAccessToken(accessToken: AccessTokenDataModel) {
        this.accessToken = accessToken

    }

    override fun getAccessToken(): AccessTokenDataModel? {
        return accessToken
    }

    override fun clearAccessToken() {
        accessToken = null
    }
}