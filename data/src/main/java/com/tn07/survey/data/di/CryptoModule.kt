package com.tn07.survey.data.di

import android.content.Context
import android.os.Build
import com.tn07.survey.data.crypto.SecretKeyManager
import com.tn07.survey.data.crypto.SecretKeyManagerImpl
import com.tn07.survey.data.crypto.SecretKeyManagerPreMImpl
import com.tn07.survey.data.di.qualifier.PreMQualifier
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by toannguyen
 * Jul 16, 2021 at 22:39
 */
@InstallIn(SingletonComponent::class)
@Module
class CryptoModule {
    @Provides
    @Singleton
    fun providesKeyManager(
        @ApplicationContext context: Context
    ): SecretKeyManager {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            SecretKeyManagerImpl()
        } else {
            SecretKeyManagerPreMImpl(context)
        }
    }

    @Provides
    @Singleton
    @PreMQualifier
    fun providesLegacyKeyManager(
        @ApplicationContext context: Context
    ): SecretKeyManager {
        return SecretKeyManagerPreMImpl(context)
    }
}