package com.dong.floatmediaplayer.bean.wangyi

import com.google.gson.annotations.SerializedName

data class SongListResponse(
        @SerializedName("code")
        val code: Int,
        @SerializedName("data")
        val `data`: Data,
        @SerializedName("result")
        val result: String
)