package com.tn07.survey.domain.entities

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

/**
 * Created by toannguyen
 * Jul 14, 2021 at 22:26
 */
internal class TokenKtTest {

    @Test
    fun isExpired_false() {
        val currentTimeSeconds = System.currentTimeMillis() / 1000

        val accessToken = Mockito.mock(AccessToken::class.java)
        Mockito.`when`(accessToken.createdAt).thenReturn(currentTimeSeconds - 100)
        Mockito.`when`(accessToken.expiresIn).thenReturn(200)

        Assertions.assertFalse(accessToken.isExpired)
    }

    @Test
    fun isExpired_true() {
        val currentTimeSeconds = System.currentTimeMillis() / 1000

        val accessToken = Mockito.mock(AccessToken::class.java)
        Mockito.`when`(accessToken.createdAt).thenReturn(currentTimeSeconds - 200)
        Mockito.`when`(accessToken.expiresIn).thenReturn(100)

        Assertions.assertTrue(accessToken.isExpired)
    }

    @Test
    fun getAuthorizationHeader() {
        val accessToken = Mockito.mock(AccessToken::class.java)
        Mockito.`when`(accessToken.tokenType).thenReturn("[TOKEN-TYPE]")
        Mockito.`when`(accessToken.accessToken).thenReturn("[ACCESS-TOKEN]")

        val authorizationHeader = accessToken.authorizationHeader

        Assertions.assertEquals("[TOKEN-TYPE] [ACCESS-TOKEN]", authorizationHeader)
    }
}