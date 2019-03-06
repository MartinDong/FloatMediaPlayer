package com.dong.floatmediaplayer.contract

import com.dong.floatmediaplayer.base.BaseView
import com.dong.floatmediaplayer.bean.SongHiBaiBody
import com.dong.floatmediaplayer.bean.SongHiBaiResponse
import io.reactivex.Flowable

interface SongHiBaiListContract {
    interface Model {
        fun getSongList(): Flowable<SongHiBaiResponse>?
    }

    interface View : BaseView {
        fun onPlaySong(song: SongHiBaiBody)
    }

    interface Presenter {
        fun getSongList()
    }
}