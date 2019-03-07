package com.dong.floatmediaplayer.bean.wangyi

import android.os.Parcel
import android.os.Parcelable
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
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readInt(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readLong(),
        source.readInt(),
        source.createTypedArrayList(Song.CREATOR)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(songListCount)
        writeString(songListDescription)
        writeString(songListId)
        writeString(songListName)
        writeString(songListPic)
        writeLong(songListPlayCount)
        writeInt(songListUserId)
        writeTypedList(songs)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Data> = object : Parcelable.Creator<Data> {
            override fun createFromParcel(source: Parcel): Data = Data(source)
            override fun newArray(size: Int): Array<Data?> = arrayOfNulls(size)
        }
    }
}