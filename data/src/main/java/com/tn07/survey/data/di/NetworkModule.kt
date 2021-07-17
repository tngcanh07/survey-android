package com.tn07.survey.data.di

import com.tn07.survey.data.api.ApiConfig
import com.tn07.survey.data.api.ApiRequestAuthenticator
import com.tn07.survey.data.api.ApiRequestInterceptor
import com.tn07.survey.data.api.NETWORK_CONNECT_TIME_OUT
import com.tn07.survey.data.api.NETWORK_READ_TIME_OUT
import com.tn07.survey.data.api.NETWORK_WRITE_TIME_OUT
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created by toannguyen
 * Jul 15, 2021 at 00:13
 */
@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun providesRetrofit(
        apiConfig: ApiConfig,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(apiConfig.baseUrl)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(
        apiConfig: ApiConfig,
        interceptor: Interceptor,
        authenticator: Authenticator
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .authenticator(authenticator)
            .readTimeout(NETWORK_READ_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(NETWORK_WRITE_TIME_OUT, TimeUnit.SECONDS)
            .connectTimeout(NETWORK_CONNECT_TIME_OUT, TimeUnit.SECONDS)

        if (apiConfig.isLoggingEnabled) {
            val log = HttpLoggingInterceptor()
            log.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(log)
        }
        return builder.build()
    }

    @Singleton
    @Provides
    fun providesApiAuthenticator(impl: ApiRequestAuthenticator): Authenticator {
        return impl
    }

    @Singleton
    @Provides
    fun providesApiInterceptor(impl: ApiRequestInterceptor): Interceptor {
        return impl
    }
}
