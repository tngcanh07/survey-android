package com.tn07.survey.domain.entities

/**
 * Created by toannguyen
 * Jul 13, 2021 at 20:56
 */
sealed interface Token

object AnonymousToken : Token

interface AccessToken : Token {
    val accessToken: String
    val refreshToken: String
    val tokenType: String
    val expiresIn: Long
    val createdAt: Long
}

val AccessToken.isExpired: Boolean
    get() {
        val currentTimeSeconds = System.currentTimeMillis() / 1000
        return currentTimeSeconds >= expiresIn + createdAt
    }

val AccessToken.authorizationHeader: String
    get() = "$tokenType $accessToken"