package com.tn07.survey.data.oauth.datasources.local

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.tn07.survey.data.crypto.LegacySecretKeyManager
import com.tn07.survey.data.crypto.SecretKeyManager
import com.tn07.survey.data.oauth.models.AccessTokenDataModel
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import java.security.KeyException
import java.security.SecureRandom
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

/**
 * Created by toannguyen
 * Jul 23, 2021 at 12:22
 */
@RunWith(RobolectricTestRunner::class)
class OAuthLocalDataSourceImplTest {

    private lateinit var dataSource: OAuthLocalDataSourceImpl
    private lateinit var secretKeyManager: SecretKeyManager
    private lateinit var legacyKeyManager: LegacySecretKeyManager
    private lateinit var secretKey: SecretKey

    private val context: Context
        get() = InstrumentationRegistry.getInstrumentation().context

    @Before
    fun setUp() {
        secretKeyManager = Mockito.mock(SecretKeyManager::class.java)
        legacyKeyManager = Mockito.mock(LegacySecretKeyManager::class.java)
        dataSource = OAuthLocalDataSourceImpl(context, secretKeyManager, legacyKeyManager)

        val keyGen: KeyGenerator = KeyGenerator.getInstance("AES")
        val random = SecureRandom()
        keyGen.init(random)
        secretKey = keyGen.generateKey()

        Mockito.`when`(secretKeyManager.getOrCreateSecretKey(Mockito.anyString()))
            .thenReturn(secretKey)
        Mockito.`when`(secretKeyManager.aesCipherAlgorithm).thenReturn("AES/CBC/PKCS5Padding")
    }

    @Test
    fun `store, get, and clear access token successfully`() {
        val accessToken = AccessTokenDataModel(
            accessToken = "fake-access-token",
            tokenType = "fake-token-type",
            refreshToken = "fake-refresh-token",
            expiresIn = 123456,
            createdAt = 234959
        )
        dataSource.storeAccessToken(accessToken)

        // from memory
        val memoryAccessToken = dataSource.getAccessToken()
        Assert.assertEquals(accessToken, memoryAccessToken)

        // from disk
        val diskAccessToken = OAuthLocalDataSourceImpl(context, secretKeyManager, legacyKeyManager)
            .getAccessToken()
        Assert.assertEquals(accessToken, diskAccessToken)

        // clear access token
        dataSource.clearAccessToken()
        Assert.assertNull(dataSource.getAccessToken())
        Assert.assertNull(
            OAuthLocalDataSourceImpl(context, secretKeyManager, legacyKeyManager)
                .getAccessToken()
        )
    }

    @Test
    fun `get access token, returns default null access token`() {
        Assert.assertNull(dataSource.getAccessToken())
    }

    @Test
    fun `keyManager throw exception, return default NULL access token`() {
        val exception = KeyException()
        Mockito.`when`(secretKeyManager.getOrCreateSecretKey(Mockito.anyString()))
            .thenAnswer { throw exception }

        dataSource = OAuthLocalDataSourceImpl(context, secretKeyManager, legacyKeyManager)

        Assert.assertNull(dataSource.getAccessToken())
    }

    @Test(expected = KeyException::class)
    fun `keyManager throw exception, save access token error`() {
        val accessToken = AccessTokenDataModel(
            accessToken = "fake-access-token",
            tokenType = "fake-token-type",
            refreshToken = "fake-refresh-token",
            expiresIn = 123456,
            createdAt = 234959
        )
        val exception = KeyException()
        Mockito.`when`(secretKeyManager.getOrCreateSecretKey(Mockito.anyString()))
            .thenAnswer { throw exception }

        dataSource.storeAccessToken(accessToken)
    }
}