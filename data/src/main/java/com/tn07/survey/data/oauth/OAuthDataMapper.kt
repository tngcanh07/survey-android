package com.tn07.survey.data.oauth

import com.tn07.survey.data.oauth.models.AccessTokenDataModel
import com.tn07.survey.domain.entities.AccessToken

/**
 * Created by toannguyen
 * Jul 14, 2021 at 23:33
 */

internal fun AccessTokenDataModel.toAccessToken(): AccessToken {
    return this
}