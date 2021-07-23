package com.tn07.survey.data.crypto

import android.annotation.TargetApi
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

/**
 * Created by toannguyen
 * Jul 16, 2021 at 21:16
 */
@TargetApi(Build.VERSION_CODES.M)
class SecretKeyManagerImpl : SecretKeyManager {

    override val aesCipherAlgorithm: String
        get() = "AES/CBC/PKCS7Padding"

    private val androidKeyStore by lazy {
        KeyStore.getInstance(ANDROID_KEY_STORE).apply {
            load(null)
        }
    }


    override fun getOrCreateSecretKey(keyAlias: String): SecretKey {
        if (!androidKeyStore.containsAlias(keyAlias)) {
            val aesSpec = KeyGenParameterSpec.Builder(
                keyAlias,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .setKeySize(256)
                .build()

            val keyGenerator =
                KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE)
            keyGenerator.init(aesSpec)
            keyGenerator.generateKey()
        }

        val keyEntry = androidKeyStore.getEntry(keyAlias, null) as KeyStore.SecretKeyEntry
        return keyEntry.secretKey
    }
}