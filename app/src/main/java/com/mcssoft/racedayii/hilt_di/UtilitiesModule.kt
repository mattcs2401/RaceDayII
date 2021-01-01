package com.mcssoft.racedayii.hilt_di

import android.content.Context
import com.mcssoft.racedayii.utiliy.RaceDayUtilities
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object UtilitiesModule {

    @Singleton
    @Provides
    fun provideRaceDayUtilities(@ApplicationContext context: Context): RaceDayUtilities {
        return RaceDayUtilities(context)
    }
}
