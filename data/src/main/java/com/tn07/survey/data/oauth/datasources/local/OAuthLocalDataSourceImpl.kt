package com.tn07.survey.data.oauth.datasources.local

import android.content.Context
import android.content.SharedPreferences
import com.tn07.survey.data.crypto.SecretKeyManager
import com.tn07.survey.data.crypto.decryptAes
import com.tn07.survey.data.crypto.encryptAes
import com.tn07.survey.data.oauth.models.AccessTokenDataModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.security.GeneralSecurityException
import javax.inject.Inject

/**
 * Created by toannguyen
 * Jul 14, 2021 at 23:08
 */
class OAuthLocalDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val secretKeyManager: SecretKeyManager
) : OAuthLocalDataSource {

    private val locker = Any()

    private val sharedPreferences: SharedPreferences
        get() = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private var accessToken: AccessTokenDataModel? = null

    init {
        accessToken = try {
            loadAccessToken()
        } catch (e: GeneralSecurityException) {
            null
        }
    }

    override fun storeAccessToken(accessToken: AccessTokenDataModel) {
        synchronized(locker) {
            this.accessToken = accessToken
            saveAccessToken(accessToken)
        }
    }

    override fun getAccessToken(): AccessTokenDataModel? {
        return accessToken
    }

    override fun clearAccessToken() {
        accessToken = null
        synchronized(locker) {
            sharedPreferences.edit()
                .clear()
                .apply()
        }
    }

    private fun saveAccessToken(accessToken: AccessTokenDataModel) {
        val secretKey = secretKeyManager.getOrCreateSecretKey(KEY_STORE_ALIAS)
        val algorithm = secretKeyManager.aesCipherAlgorithm
        sharedPreferences.edit()
            .apply {
                putString(
                    KEY_ACCESS_TOKEN,
                    encryptAes(accessToken.accessToken, secretKey, algorithm)
                )
                putString(
                    KEY_TOKEN_TYPE,
                    encryptAes(accessToken.tokenType, secretKey, algorithm)
                )
                putString(
                    KEY_REFRESH_TOKEN,
                    encryptAes(accessToken.refreshToken, secretKey, algorithm)
                )
                putLong(KEY_CREATED_AT, accessToken.createdAt)
                putLong(KEY_EXPIRES_IN, accessToken.expiresIn)
            }
            .apply()

    }

    @Throws(GeneralSecurityException::class)
    private fun loadAccessToken(): AccessTokenDataModel? {
        val secretKey = secretKeyManager.getOrCreateSecretKey(KEY_STORE_ALIAS)
        val algorithm = secretKeyManager.aesCipherAlgorithm

        with(sharedPreferences) {
            val accessToken = getString(KEY_ACCESS_TOKEN, null)
                ?.let { decryptAes(it, secretKey, algorithm) }

            val tokenType = getString(KEY_TOKEN_TYPE, null)
                ?.let { decryptAes(it, secretKey, algorithm) }

            val refreshToken = getString(KEY_REFRESH_TOKEN, null)
                ?.let { decryptAes(it, secretKey, algorithm) }

            val createdAt = getLong(KEY_CREATED_AT, 0)

            val expiresIn = getLong(KEY_EXPIRES_IN, 0)

            return if (accessToken != null
                && tokenType != null
                && refreshToken != null
                && createdAt != 0L
                && expiresIn != 0L
            ) {
                AccessTokenDataModel(
                    accessToken = accessToken,
                    tokenType = tokenType,
                    refreshToken = refreshToken,
                    createdAt = createdAt,
                    expiresIn = expiresIn
                )
            } else {
                null
            }
        }
    }

    companion object {
        private const val KEY_STORE_ALIAS = "oauth2"
        private const val PREFS_NAME = "survey.prefs.oauth"
        private const val KEY_ACCESS_TOKEN = "survey.prefs.oauth.accessToken"
        private const val KEY_TOKEN_TYPE = "survey.prefs.oauth.tokenType"
        private const val KEY_REFRESH_TOKEN = "survey.prefs.oauth.refreshToken"
        private const val KEY_EXPIRES_IN = "survey.prefs.oauth.expiresIn"
        private const val KEY_CREATED_AT = "survey.prefs.oauth.createdAT"
    }
}
