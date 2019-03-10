package com.dong.floatmediaplayer.contract

import com.dong.floatmediaplayer.base.BaseView
import com.dong.floatmediaplayer.bean.wangyi.Song

interface SongDetailContract {
    interface Model {
    }

    interface View : BaseView {
        fun showSongDetail(song: Song)
        fun onPlaySong(song: Song)
        fun pauseSong(song: Song)
    }

    interface Presenter {

    }
}