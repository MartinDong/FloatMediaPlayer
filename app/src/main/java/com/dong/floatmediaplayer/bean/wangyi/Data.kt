package com.dong.floatmediaplayer.bean.wangyi

import com.google.gson.annotations.SerializedName

data class Data(
        @SerializedName("songListCount")
        val songListCount: Int,
        @SerializedName("songListDescription")
        val songListDescription: String,
        @SerializedName("songListId")
        val songListId: String,
        @SerializedName("songListName")
        val songListName: String,
        @SerializedName("songListPic")
        val songListPic: String,
        @SerializedName("songListPlayCount")
        val songListPlayCount: Long,
        @SerializedName("songListUserId")
        val songListUserId: Int,
        @SerializedName("songs")
        val songs: List<Song>
)