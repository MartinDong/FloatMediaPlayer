package com.dong.floatmediaplayer.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.text.TextUtils
import com.dong.floatmediaplayer.bean.wangyi.Song

class SongPlayerService : Service() {

    private var mSongList: MutableList<Song>? = null
    private var mMediaPlayer: MediaPlayer? = null
    private var mSongBinder: SongBinder? = null
    private var mCurrentPlaySongIndex = 0

    override fun onCreate() {
        println("---初始化音乐播放服务---onCreate")
        super.onCreate()
        mSongList = mutableListOf()
        mMediaPlayer = MediaPlayer()
        mSongBinder = SongBinder()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        println("---初始化音乐播放服务---onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder {
        println("---初始化音乐播放服务---onBind")
        return SongBinder()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        println("---音乐播放服务解綁---onUnbind")
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        println("---音乐播放服务停止---onDestroy")
        super.onDestroy()
        if (mMediaPlayer != null) {
            mMediaPlayer!!.stop()
            mMediaPlayer!!.release()
        }
        mSongList = null
        mSongBinder = null
    }

    inner class SongBinder : Binder() {
        fun initSongList(songList: MutableList<Song>) {
            mSongList = songList
        }

        fun play(song: Song): Boolean {
            println("---音乐播放服务---play--$song")
            if (mMediaPlayer != null) {
                mCurrentPlaySongIndex = getSongIndex(song)
                val songUrl = song.url
                if (!TextUtils.isEmpty(songUrl)) {
                    mMediaPlayer!!.reset()
                    mMediaPlayer!!.setDataSource(songUrl)

                    mMediaPlayer!!.prepareAsync()
                    mMediaPlayer!!.setOnPreparedListener {
                        it.start()
                    }

                    mMediaPlayer!!.setOnCompletionListener {
                        println("---当前音乐播放完闭，开始播放下一首---")
                        mCurrentPlaySongIndex++
                        if (mCurrentPlaySongIndex <= mSongList!!.count()) {
                            val nextSong = mSongList!![mCurrentPlaySongIndex]
                            play(nextSong)
                        } else {
                            pause()
                        }
                    }
                    return true
                } else {
                    pause()
                    return false
                }
            }
            return false
        }

        fun pause(): Boolean {
            println("---音乐播放服务---pause--")
            return if (mMediaPlayer != null) {
                if (mMediaPlayer!!.isPlaying) {
                    mMediaPlayer!!.pause()
                }
                mMediaPlayer!!.isPlaying
            } else {
                true
            }
        }

        fun playNext() {
            mCurrentPlaySongIndex++
            if (mCurrentPlaySongIndex <= mSongList!!.count()) {
                val nextSong = mSongList!![mCurrentPlaySongIndex]
                play(nextSong)
            } else {
                pause()
            }
        }

        private fun getSongIndex(targetSong: Song): Int {
            var songIndex = 0
            mSongList!!.forEachIndexed { index, song ->
                if (targetSong.id == song.id) {
                    songIndex = index
                }
            }
            return songIndex
        }
    }

}