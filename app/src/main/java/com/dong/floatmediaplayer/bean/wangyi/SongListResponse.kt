package com.dong.floatmediaplayer.bean.wangyi

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class SongListResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("result")
    val result: String
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readInt(),
        source.readParcelable<Data>(Data::class.java.classLoader),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(code)
        writeParcelable(data, 0)
        writeString(result)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<SongListResponse> = object : Parcelable.Creator<SongListResponse> {
            override fun createFromParcel(source: Parcel): SongListResponse = SongListResponse(source)
            override fun newArray(size: Int): Array<SongListResponse?> = arrayOfNulls(size)
        }
    }
}