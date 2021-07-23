package com.tn07.survey.data.api

import com.tn07.survey.domain.entities.AccessToken
import com.tn07.survey.domain.entities.authorizationHeader
import com.tn07.survey.domain.repositories.OAuthRepository
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

/**
 * Created by toannguyen
 * Jul 15, 2021 at 00:08
 */
class ApiRequestAuthenticator @Inject constructor(
    private val repository: OAuthRepository
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        return refreshToken(response)?.let {
            response.request
                .newBuilder()
                .header(HEADER_AUTHORIZATION, it.authorizationHeader)
                .build()
        }
    }

    @Synchronized
    private fun refreshToken(response: Response): AccessToken? {
        return if (response.priorResponse == null) {
            val storedToken = repository.getToken() as? AccessToken
            storedToken?.let {
                repository.refreshToken(it)
                    .onErrorComplete()
                    .blockingGet()
            }
        } else {
            null
        }
    }
}
