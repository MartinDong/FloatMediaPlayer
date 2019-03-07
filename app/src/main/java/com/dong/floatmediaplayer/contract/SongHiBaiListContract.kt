package com.dong.floatmediaplayer.contract

import com.dong.floatmediaplayer.base.BaseView
import com.dong.floatmediaplayer.bean.wangyi.Song
import com.dong.floatmediaplayer.bean.wangyi.SongListResponse
import io.reactivex.Flowable

interface SongHiBaiListContract {
    interface Model {
        fun getSongList(): Flowable<SongListResponse>?
    }

    interface View : BaseView {
        fun onPlaySong(song: Song)
    }

    interface Presenter {
        fun getSongList()
    }
}