package com.tn07.survey.data.api

import com.tn07.survey.domain.entities.AccessToken
import com.tn07.survey.domain.entities.authorizationHeader
import com.tn07.survey.domain.entities.isExpired
import com.tn07.survey.domain.repositories.OAuthRepository
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * Created by toannguyen
 * Jul 15, 2021 at 00:08
 */
class ApiRequestInterceptor @Inject constructor(
    private val repository: OAuthRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request()
            .newBuilder()
        getAccessToken()?.let {
            requestBuilder.addHeader(HEADER_AUTHORIZATION, it.authorizationHeader)
        }
        return chain.proceed(requestBuilder.build())
    }

    @Synchronized
    private fun getAccessToken(): AccessToken? {
        val token = repository.getToken()
        return when {
            token !is AccessToken -> null
            token.isExpired -> repository.refreshToken(token)
                .onErrorComplete()
                .blockingGet(null)
            else -> token
        }
    }
}
