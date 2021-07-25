package com.tn07.survey.data.crypto

import android.content.Context
import android.util.Base64
import java.io.File
import java.security.GeneralSecurityException
import java.security.SecureRandom
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.inject.Inject

/**
 * Created by toannguyen
 * Jul 16, 2021 at 21:16
 */
private const val KEY_ALIAS_SUFFIX = "PreM"

class SecretKeyManagerPreMImpl @Inject constructor(
    private val context: Context
) : SecretKeyManager {

    override val aesCipherAlgorithm: String
        get() = "AES/CBC/PKCS5Padding"

    override fun isExist(keyAlias: String): Boolean {
        return getKeyFile(keyAlias).exists()
    }

    override fun delete(keyAlias: String) {
        getKeyFile(keyAlias).delete()
    }

    override fun getOrCreateSecretKey(keyAlias: String): SecretKey {
        val alias = "${keyAlias}${KEY_ALIAS_SUFFIX}"
        val keyFile = File(
            context.filesDir,
            Base64.encodeToString(alias.toByteArray(Charsets.UTF_8), Base64.DEFAULT)
        )
        val secretKeyWrapper = SecretKeyWrapper(context, alias)
        if (keyFile.exists()) {
            try {
                return secretKeyWrapper.unwrap(keyFile.readBytes())
            } catch (e: GeneralSecurityException) {
                keyFile.delete()
            }
        }
        keyFile.createNewFile()

        // generate new secret key
        val secretKey = generateSecretKey()
        val wrappedSecretKey = secretKeyWrapper.wrap(secretKey)
        keyFile.writeBytes(wrappedSecretKey)

        // read secret key from file
        return secretKeyWrapper.unwrap(keyFile.readBytes())
    }

    private fun getKeyFile(keyAlias: String): File {
        val alias = "${keyAlias}${KEY_ALIAS_SUFFIX}"
        return File(
            context.filesDir,
            Base64.encodeToString(alias.toByteArray(Charsets.UTF_8), Base64.DEFAULT)
        )
    }

    private fun generateSecretKey(): SecretKey {
        val secureRandom = SecureRandom()
        val keyGenerator: KeyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(AES_KEY_SIZE, secureRandom)
        return keyGenerator.generateKey()
    }
}
