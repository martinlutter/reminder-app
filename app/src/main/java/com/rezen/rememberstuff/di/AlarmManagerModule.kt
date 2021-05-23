package com.rezen.rememberstuff.di

import android.app.AlarmManager
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AlarmManagerModule {
    @Provides
    fun provideAlarmManager(@ApplicationContext context: Context): AlarmManager {
        return context.getSystemService(AlarmManager::class.java)
    }
}