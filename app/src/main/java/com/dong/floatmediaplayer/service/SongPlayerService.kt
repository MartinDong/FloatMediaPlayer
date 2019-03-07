package com.dong.floatmediaplayer.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.dong.floatmediaplayer.bean.wangyi.Song

class SongPlayerService : Service() {

    private lateinit var mSong: Song

    override fun onBind(intent: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}