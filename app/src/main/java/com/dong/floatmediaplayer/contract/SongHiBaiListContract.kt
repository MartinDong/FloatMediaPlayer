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
        fun onPauseSong(song: Song)
        fun continuePlay(song: Song)
        fun onSuccess(message: Any)
    }

    interface Presenter {
        fun getSongList()
    }
}