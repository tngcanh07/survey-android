package com.tn07.survey.data.crypto

import javax.crypto.SecretKey

/**
 * Created by toannguyen
 * Jul 16, 2021 at 21:14
 */
interface SecretKeyManager {
    val aesCipherAlgorithm: String

    fun getOrCreateSecretKey(keyAlias: String): SecretKey
}