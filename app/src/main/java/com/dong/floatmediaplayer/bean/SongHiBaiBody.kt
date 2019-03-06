package com.dong.floatmediaplayer.bean

import com.google.gson.annotations.SerializedName

data class SongHiBaiBody(
    @SerializedName("author")
    val author: String,
    @SerializedName("lrc")
    val lrc: String,
    @SerializedName("pic")
    val pic: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("url")
    val url: String
)