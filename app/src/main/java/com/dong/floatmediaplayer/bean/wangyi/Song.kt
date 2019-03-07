package com.dong.floatmediaplayer.bean.wangyi

import com.google.gson.annotations.SerializedName

data class Song(
        @SerializedName("id")
        val id: String,
        @SerializedName("lrc")
        val lrc: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("pic")
        val pic: String,
        @SerializedName("singer")
        val singer: String,
        @SerializedName("time")
        val time: Int,
        @SerializedName("url")
        val url: String
)