package com.tn07.survey.data.di

import com.google.gson.Gson
import com.tn07.survey.data.api.NETWORK_CONNECT_TIME_OUT
import com.tn07.survey.data.api.NETWORK_READ_TIME_OUT
import com.tn07.survey.data.api.NETWORK_WRITE_TIME_OUT
import com.tn07.survey.data.api.OAuthConfig
import com.tn07.survey.data.di.qualifier.OAuthQualifier
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created by toannguyen
 * Jul 14, 2021 at 23:54
 */
@Module
@InstallIn(SingletonComponent::class)
class OAuthNetworkModule {
    @Singleton
    @Provides
    @OAuthQualifier
    fun providesOkHttpClient(oauthConfig: OAuthConfig): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .readTimeout(NETWORK_READ_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(NETWORK_WRITE_TIME_OUT, TimeUnit.SECONDS)
            .connectTimeout(NETWORK_CONNECT_TIME_OUT, TimeUnit.SECONDS)
        if (oauthConfig.isLoggingEnabled) {
            val log = HttpLoggingInterceptor()
            log.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(log)
        }
        return builder.build()
    }

    @Singleton
    @Provides
    @OAuthQualifier
    fun providesRetrofit(
        oauthConfig: OAuthConfig,
        gson: Gson,
        @OAuthQualifier okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(oauthConfig.baseUrl)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}