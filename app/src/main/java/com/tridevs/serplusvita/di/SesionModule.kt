package com.tridevs.serplusvita.di

import android.content.Context
import com.tridevs.serplusvita.utils.SesionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SesionModule {

    @Provides
    @Singleton
    fun provideSesionManager(@ApplicationContext context: Context): SesionManager {
        return SesionManager(context)
    }
}