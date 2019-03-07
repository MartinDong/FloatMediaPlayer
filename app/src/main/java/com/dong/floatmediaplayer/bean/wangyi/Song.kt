package com.dong.floatmediaplayer.bean.wangyi

import android.os.Parcel
import android.os.Parcelable
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
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readInt(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(lrc)
        writeString(name)
        writeString(pic)
        writeString(singer)
        writeInt(time)
        writeString(url)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Song> = object : Parcelable.Creator<Song> {
            override fun createFromParcel(source: Parcel): Song = Song(source)
            override fun newArray(size: Int): Array<Song?> = arrayOfNulls(size)
        }
    }
}