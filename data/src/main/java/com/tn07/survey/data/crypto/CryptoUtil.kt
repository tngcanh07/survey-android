package com.tn07.survey.data.crypto

import android.util.Base64
import java.security.AlgorithmParameters
import java.security.GeneralSecurityException
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

/**
 * Created by toannguyen
 * Jul 16, 2021 at 15:46
 */
private const val SEPARATOR = ";"
private const val BASE64_FLAG = Base64.NO_WRAP
const val ANDROID_KEY_STORE = "AndroidKeyStore"
const val AES_KEY_SIZE = 256

@Throws(GeneralSecurityException::class)
internal fun encryptAes(value: String, secret: SecretKey, algorithm: String): String {
    val cipher = Cipher.getInstance(algorithm)
    cipher.init(Cipher.ENCRYPT_MODE, secret)
    val params: AlgorithmParameters = cipher.parameters
    val iv: ByteArray = params.getParameterSpec(IvParameterSpec::class.java).iv
    val cipherData = cipher.doFinal(value.toByteArray(Charsets.UTF_8))
    val cipherString = Base64.encodeToString(cipherData, BASE64_FLAG)
    val ivString = Base64.encodeToString(iv, BASE64_FLAG)
    return "$ivString$SEPARATOR$cipherString"
}

@Throws(GeneralSecurityException::class)
internal fun decryptAes(value: String, secret: SecretKey, algorithm: String): String {
    val fields = value.split(SEPARATOR).toTypedArray()
    if (fields.size != 2) {
        throw GeneralSecurityException("value must contain IV!")
    }
    val iv: ByteArray = Base64.decode(fields[0], BASE64_FLAG)
    val cipherBytes: ByteArray = Base64.decode(fields[1], BASE64_FLAG)

    val ivParams = IvParameterSpec(iv)
    val cipher = Cipher.getInstance(algorithm)
    cipher.init(Cipher.DECRYPT_MODE, secret, ivParams)
    return String(cipher.doFinal(cipherBytes), Charsets.UTF_8)
}