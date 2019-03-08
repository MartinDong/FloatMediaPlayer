package com.dong.floatmediaplayer.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dong.floatmediaplayer.R
import com.dong.floatmediaplayer.bean.wangyi.Song

class SongHiBaiListAdapter(private var songList: List<Song>, private var operationListener: OperationListener) :
    RecyclerView.Adapter<SongHiBaiListAdapter.SongHiBaiHV>() {

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

    class SongHiBaiHV(itemView: View, private var operationListener: OperationListener) :
        RecyclerView.ViewHolder(itemView) {
        fun initViewDate(song: Song) {
            itemView.findViewById<TextView>(R.id.tv_song_name).text = song.name
            itemView.findViewById<TextView>(R.id.tv_song_singer).text = song.singer

            itemView.setOnClickListener {
                operationListener.playSong(song)
            }
        }
    }

    interface OperationListener {
        fun playSong(song: Song)
        fun pauseSong(song: Song)
    }
}
