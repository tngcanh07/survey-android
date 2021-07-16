package com.tn07.survey.data.crypto

import android.content.Context
import android.security.KeyPairGeneratorSpec
import java.math.BigInteger
import java.security.GeneralSecurityException
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.util.Calendar
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.security.auth.x500.X500Principal

/**
 * Wraps [SecretKey] instances using a public/private key pair stored in
 * the platform [KeyStore]. This allows us to protect symmetric keys with
 * hardware-backed crypto, if provided by the device.
 *
 *
 * See [key wrapping](http://en.wikipedia.org/wiki/Key_Wrap) for more
 * details.
 *
 *
 * Not inherently thread safe.
 */
class SecretKeyWrapper(context: Context, alias: String) {
    private val mCipher: Cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
    private val mPair: KeyPair

    /**
     * Create a wrapper using the public/private key pair with the given alias.
     * If no pair with that alias exists, it will be generated.
     */
    init {
        val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE)
        keyStore.load(null)
        if (!keyStore.containsAlias(alias)) {
            generateKeyPair(context, alias)
        }

        // Even if we just generated the key, always read it back to ensure we
        // can read it successfully.
        val entry = keyStore.getEntry(alias, null) as KeyStore.PrivateKeyEntry
        mPair = KeyPair(entry.certificate.publicKey, entry.privateKey)
    }

    /**
     * Wrap a [SecretKey] using the public key assigned to this wrapper.
     * Use [.unwrap] to later recover the original
     * [SecretKey].
     *
     * @return a wrapped version of the given [SecretKey] that can be
     * safely stored on untrusted storage.
     */
    @Throws(GeneralSecurityException::class)
    fun wrap(key: SecretKey): ByteArray {
        mCipher.init(Cipher.WRAP_MODE, mPair.public)
        return mCipher.wrap(key)
    }

    /**
     * Unwrap a [SecretKey] using the private key assigned to this
     * wrapper.
     *
     * @param blob a wrapped [SecretKey] as previously returned by
     * [.wrap].
     */
    @Throws(GeneralSecurityException::class)
    fun unwrap(blob: ByteArray): SecretKey {
        mCipher.init(Cipher.UNWRAP_MODE, mPair.private)
        return mCipher.unwrap(blob, "AES", Cipher.SECRET_KEY) as SecretKey
    }

    @Throws(GeneralSecurityException::class)
    private fun generateKeyPair(context: Context, alias: String) {
        val start: Calendar = Calendar.getInstance()
        val end: Calendar = Calendar.getInstance()
        end.add(Calendar.YEAR, 1)
        val spec = KeyPairGeneratorSpec.Builder(context)
            .setAlias(alias)
            .setSubject(X500Principal("CN=$alias"))
            .setSerialNumber(BigInteger.ONE)
            .setStartDate(start.time)
            .setEndDate(end.time)
            .build()
        val gen = KeyPairGenerator.getInstance("RSA", ANDROID_KEY_STORE)
        gen.initialize(spec)
        gen.generateKeyPair()
    }
}