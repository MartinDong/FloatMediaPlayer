package com.dong.floatmediaplayer.model

import com.dong.floatmediaplayer.R
import com.dong.floatmediaplayer.SongApplication
import com.dong.floatmediaplayer.bean.SongHiBaiResponse
import com.dong.floatmediaplayer.contract.SongHiBaiListContract
import com.google.gson.Gson
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable


class SongHiBaiListModel : SongHiBaiListContract.Model {

    override fun getSongList(): Flowable<SongHiBaiResponse>? {
        val application = SongApplication.getApplication()
        val inputStream = application?.resources?.openRawResource(R.raw.media_config)
        val bytes = inputStream?.readBytes()
        if (bytes != null) {
            val responseJson = String(bytes)
            return Flowable
                .create({ e ->
                    e.onNext(
                        Gson().fromJson(
                            responseJson,
                            SongHiBaiResponse::class.java
                        )
                    )
                    e.onComplete()
                }, BackpressureStrategy.BUFFER)
        }
        return null
    }
}