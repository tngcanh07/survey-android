package com.tn07.survey.data.crypto

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by toannguyen
 * Jul 23, 2021 at 13:30
 */
@RunWith(AndroidJUnit4::class)
class SecretKeyManagerImplTest {

    private lateinit var secretKeyManager: SecretKeyManagerImpl

    @Before
    fun setUp() {
        secretKeyManager = SecretKeyManagerImpl()
    }

    @Test
    fun encryptAndDecryptData() {
        val text = "this is a text message!!!!!"

        // encrypt
        val encryptKey = secretKeyManager.getOrCreateSecretKey("test")
        val encryptedData = encryptAes(text, encryptKey, secretKeyManager.aesCipherAlgorithm)

        // decrypt
        val decryptKey = secretKeyManager.getOrCreateSecretKey("test")
        val decryptedData = decryptAes(
            value = encryptedData,
            secret = decryptKey,
            algorithm = secretKeyManager.aesCipherAlgorithm
        )

        Assert.assertEquals(text, decryptedData)
    }
}