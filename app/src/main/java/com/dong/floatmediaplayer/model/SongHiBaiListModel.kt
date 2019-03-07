package com.dong.floatmediaplayer.model

import com.dong.floatmediaplayer.R
import com.dong.floatmediaplayer.SongApplication
import com.dong.floatmediaplayer.bean.wangyi.SongListResponse
import com.dong.floatmediaplayer.contract.SongHiBaiListContract
import com.google.gson.Gson
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable


class SongHiBaiListModel : SongHiBaiListContract.Model {

    override fun getSongList(): Flowable<SongListResponse>? {
        val application = SongApplication.getApplication()
        val inputStream = application?.resources?.openRawResource(R.raw.wangyi_music)
        val bytes = inputStream?.readBytes()
        if (bytes != null) {
            val responseJson = String(bytes)
            return Flowable
                .create({ e ->
                    e.onNext(
                        Gson().fromJson(
                            responseJson,
                            SongListResponse::class.java
                        )
                    )
                    e.onComplete()
                }, BackpressureStrategy.BUFFER)
        }
        return null
    }
}