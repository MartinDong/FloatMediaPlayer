package com.dong.floatmediaplayer

import android.app.Application
import android.content.Context

class SongApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        mApplication = this
    }

    companion object {
        var mApplication: SongApplication? = null

        fun getApplication(): SongApplication? {
            return mApplication
        }
    }

}