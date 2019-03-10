package com.dong.floatmediaplayer.contract

import com.dong.floatmediaplayer.base.BaseView
import com.dong.floatmediaplayer.bean.wangyi.Song

interface MainContract {
    interface Model {

    }

    interface View : BaseView {
        fun onPlaySong(song: Song)
        fun pauseSong(song: Song)
    }

    interface Presenter {
        fun getSongList()
    }
}