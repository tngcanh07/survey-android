package com.tn07.survey.data.crypto

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.security.GeneralSecurityException
import java.security.SecureRandom
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

/**
 * Created by toannguyen
 * Jul 22, 2021 at 20:58
 */
@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = Config.NONE,
    sdk = [Config.OLDEST_SDK]
)
internal class CryptoUtilKtTest {
    private lateinit var secretKey: SecretKey

    @Before
    fun setUp() {
        val keyGen: KeyGenerator = KeyGenerator.getInstance("AES")
        val random = SecureRandom()
        keyGen.init(random)
        secretKey = keyGen.generateKey()
    }

    @Test
    fun encryptDecrypt_aes_success() {
        val cipherAlgorithm = "AES/CBC/PKCS5PADDING"
        val numbers = "0123456789"
        val alphabet = "abcdefghijklmnopqrstuvwxyz"
        val specialCharacters = "`~!@#$%^&*()-_=+{[]}\\|;:'\"<>.,/?"
        val plainText = "$alphabet ${alphabet.uppercase()} $specialCharacters $numbers"

        val encryptedData = encryptAes(
            secret = secretKey,
            algorithm = cipherAlgorithm,
            value = plainText
        )

        val decryptedText = decryptAes(
            secret = secretKey,
            value = encryptedData,
            algorithm = cipherAlgorithm
        )

        Assert.assertEquals(plainText, decryptedText)
    }

    @Test(expected = GeneralSecurityException::class)
    fun encryptDecrypt_aes_error_missingIV() {
        val cipherAlgorithm = "AES/CBC/PKCS5PADDING"
        val numbers = "0123456789"
        val alphabet = "abcdefghijklmnopqrstuvwxyz"
        val specialCharacters = "`~!@#$%^&*()-_=+{[]}\\|;:'\"<>.,/?"
        val plainText = "$alphabet ${alphabet.uppercase()} $specialCharacters $numbers"

        val encryptedData = encryptAes(
            secret = secretKey,
            algorithm = cipherAlgorithm,
            value = plainText
        )
            .split(";")[0]

        decryptAes(
            secret = secretKey,
            value = encryptedData,
            algorithm = cipherAlgorithm
        )
    }
}