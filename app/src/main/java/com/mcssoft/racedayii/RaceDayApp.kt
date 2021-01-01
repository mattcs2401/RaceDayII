package com.mcssoft.racedayii

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import org.greenrobot.eventbus.EventBus

@HiltAndroidApp
class RaceDayApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // https://greenrobot.org/eventbus/documentation/subscriber-index/
        EventBus.builder().addIndex(MyEventBusIndex()).installDefaultEventBus()
    }
}