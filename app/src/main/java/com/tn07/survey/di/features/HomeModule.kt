package com.tn07.survey.di.features

import com.tn07.survey.features.home.transformer.HomeTransformer
import com.tn07.survey.features.home.transformer.HomeTransformerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/**
 * Created by toannguyen
 * Jul 17, 2021 at 16:07
 */
@InstallIn(ViewModelComponent::class)
@Module
interface HomeModule {
    @Binds
    fun bindHomeTransformer(impl: HomeTransformerImpl): HomeTransformer
}