package com.tn07.survey.apitests

import com.tn07.survey.data.BuildConfig
import com.tn07.survey.data.api.NETWORK_CONNECT_TIME_OUT
import com.tn07.survey.data.api.NETWORK_READ_TIME_OUT
import com.tn07.survey.data.api.NETWORK_WRITE_TIME_OUT
import com.tn07.survey.data.api.OAuthConfig
import com.tn07.survey.data.oauth.datasources.remote.OAuthRemoteDataSourceImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by toannguyen
 * Jul 14, 2021 at 13:14
 */
class OAuthApiTest {
    private lateinit var oauthRemoteData: OAuthRemoteDataSourceImpl

    @Before
    fun setUp() {

        val oauthConfig = OAuthConfig(
            baseUrl = BuildConfig.BASE_URL,
            clientId = BuildConfig.CLIENT_ID,
            clientSecret = BuildConfig.CLIENT_SECRET,
            isLoggingEnabled = true
        )
        val okHttpClientBuilder = OkHttpClient.Builder()
            .readTimeout(NETWORK_READ_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(NETWORK_WRITE_TIME_OUT, TimeUnit.SECONDS)
            .connectTimeout(NETWORK_CONNECT_TIME_OUT, TimeUnit.SECONDS)
        if (oauthConfig.isLoggingEnabled) {
            val log = HttpLoggingInterceptor()
            log.level = HttpLoggingInterceptor.Level.BODY
            okHttpClientBuilder.addInterceptor(log)
        }
        val retrofit = Retrofit.Builder()
            .baseUrl(oauthConfig.baseUrl)
            .client(okHttpClientBuilder.build())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        oauthRemoteData = OAuthRemoteDataSourceImpl(retrofit, oauthConfig)
    }

    @Test
    fun `request, refresh, revoke access token`() {

        var accessToken = oauthRemoteData.login(
            email = BuildConfig.TEST_EMAIL,
            password = BuildConfig.TEST_PASSWORD
        )
            .test()
            .await()
            .assertNoErrors()
            .values()
            .first()

        accessToken = oauthRemoteData.refreshToken(accessToken.refreshToken)
            .test()
            .await()
            .assertNoErrors()
            .values()
            .first()

        oauthRemoteData.logout(accessToken.accessToken)
            .test()
            .await()
            .assertNoErrors()
            .assertComplete()
    }
}
