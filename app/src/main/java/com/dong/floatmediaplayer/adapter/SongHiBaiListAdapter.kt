package com.dong.floatmediaplayer.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dong.floatmediaplayer.R
import com.dong.floatmediaplayer.bean.wangyi.Song


class SongHiBaiListAdapter(private var songList: List<Song>, private var operationListener: OperationListener) :
    RecyclerView.Adapter<SongHiBaiListAdapter.SongHiBaiHV>() {

    private var mSong: Song? = null
    private var mIsPlaying = false

    fun setSong(song: Song) {
        mSong = song
        notifyDataSetChanged()
    }

    fun getSong(): Song? {
        return mSong
    }

    fun setIsPlaying(isPlaying: Boolean) {
        mIsPlaying = isPlaying
    }

    fun isPlaying(): Boolean {
        return mIsPlaying
    }

    fun getPlaySongIndex(): Int {
        songList.forEachIndexed { index, song ->
            if (mSong?.id == song.id) {
                return index
            }
        }
        return -1
    }

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongHiBaiHV {
        return SongHiBaiHV(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_song_hi_bai_list, parent, false),
            operationListener
        )
    }

    override fun getItemCount(): Int {
        return if (songList.isEmpty()) {
            0
        } else {
            songList.size
        }
    }

    override fun onBindViewHolder(holder: SongHiBaiHV, position: Int) {
        holder.initViewDate(songList[position])
    }

    inner class SongHiBaiHV(itemView: View, private var operationListener: OperationListener) :
        RecyclerView.ViewHolder(itemView) {
        fun initViewDate(song: Song) {
            itemView.findViewById<TextView>(R.id.tv_song_name).text = song.name
            itemView.findViewById<TextView>(R.id.tv_song_singer).text = song.singer
            Glide
                .with(itemView)
                .load(song.pic)
                .centerCrop()
                .into(itemView.findViewById(R.id.iv_song_pic))
            val ivOperation = itemView.findViewById<ImageView>(R.id.iv_operation)

            if (mSong?.id == song.id && mIsPlaying) {
                ivOperation.setImageResource(R.drawable.widget_pause_selector)
            } else {
                ivOperation.setImageResource(R.drawable.widget_play_selector)
            }

            itemView.findViewById<ImageView>(R.id.iv_song_pic).setOnClickListener {
                if (mSong == null) {
                    mIsPlaying = true
                    operationListener.playSong(song)
                } else {
                    if (mSong?.id != song.id) {
                        mIsPlaying = true
                        operationListener.playSong(song)
                    } else {
                        if (mIsPlaying) {
                            mIsPlaying = false
                            operationListener.pauseSong(song)
                        } else {
                            mIsPlaying = true
                            operationListener.continuePlay(song)
                        }
                    }
                }
                setSong(song)
            }
            itemView.setOnClickListener {
                if (mSong == null) {
                    mIsPlaying = true
                    operationListener.playSong(song)
                } else {
                    if (mIsPlaying) {
                        mIsPlaying = true
                        operationListener.playSong(song)
                    } else {
                        if (mSong?.id != song.id) {
                            mIsPlaying = true
                            operationListener.playSong(song)
                        } else {
                            mIsPlaying = true
                            operationListener.continuePlay(song)
                        }
                    }
                }
                setSong(song)
            }
        }
    }

    interface OperationListener {
        fun playSong(song: Song)
        fun pauseSong(song: Song)
        fun continuePlay(song: Song)
        fun jumDetail(song: Song)
    }
}
