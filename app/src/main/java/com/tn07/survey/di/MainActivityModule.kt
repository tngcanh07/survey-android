package com.tn07.survey.di

import android.app.Activity
import com.tn07.survey.features.login.LoginNavigator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

/**
 * Created by toannguyen
 * Jul 16, 2021 at 11:34
 */
@Module
@InstallIn(ActivityComponent::class)
class MainActivityModule {
    @Provides
    fun bindLoginNavigator(activity: Activity): LoginNavigator {
        return requireNotNull(activity as? LoginNavigator) {
            "${activity.javaClass.name} must implement ${LoginNavigator::class.java.simpleName}"
        }
    }
}