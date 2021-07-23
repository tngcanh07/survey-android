package com.tn07.survey.data.crypto

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by toannguyen
 * Jul 23, 2021 at 13:01
 */
@RunWith(AndroidJUnit4::class)
class SecretKeyManagerPreMImplTest {
    private lateinit var secretKeyManager: SecretKeyManagerPreMImpl

    @Before
    fun setUp() {
        secretKeyManager = SecretKeyManagerPreMImpl(
            context = InstrumentationRegistry.getInstrumentation().context
        )
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