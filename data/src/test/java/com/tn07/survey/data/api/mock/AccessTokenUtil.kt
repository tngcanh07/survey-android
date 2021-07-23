package com.tn07.survey.data.api.mock

import com.tn07.survey.domain.entities.AccessToken
import org.mockito.Mockito

/**
 * Created by toannguyen
 * Jul 23, 2021 at 11:17
 */
fun mockAccessToken(
    accessToken: String? = "TokenType",
    refreshToken: String? = null,
    tokenType: String? = null,
    expiresIn: Long? = null,
    createdAt: Long? = null
): AccessToken {
    val mock = Mockito.mock(AccessToken::class.java)

    accessToken?.let { Mockito.`when`(mock.accessToken).thenReturn(it) }
    refreshToken?.let { Mockito.`when`(mock.refreshToken).thenReturn(it) }
    tokenType?.let { Mockito.`when`(mock.tokenType).thenReturn(it) }
    expiresIn?.let { Mockito.`when`(mock.expiresIn).thenReturn(it) }
    createdAt?.let { Mockito.`when`(mock.createdAt).thenReturn(it) }

    return mock
}