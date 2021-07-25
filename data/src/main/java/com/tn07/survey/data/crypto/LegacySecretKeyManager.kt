package com.tn07.survey.data.crypto

/**
 * Created by toannguyen
 * Jul 16, 2021 at 21:14
 */
interface LegacySecretKeyManager : SecretKeyManager {

    fun isExist(keyAlias: String): Boolean

    fun delete(keyAlias: String)
}