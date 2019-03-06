package com.dong.floatmediaplayer.bean

import com.google.gson.annotations.SerializedName

data class SongHiBaiResponse(
    @SerializedName("Body")
    val body: List<SongHiBaiBody>,
    @SerializedName("ErrCode")
    val errCode: String,
    @SerializedName("ResultCode")
    val resultCode: Int
)