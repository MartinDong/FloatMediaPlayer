package com.dong.floatmediaplayer.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.text.TextUtils
import com.dong.floatmediaplayer.SongApplication
import com.dong.floatmediaplayer.bean.wangyi.Song

class SongPlayerService : Service() {

    private var mSongList: MutableList<Song>? = null
    private var mCurrentSong: Song? = null
    private var mCurrentPlaySongIndex = 0

    private var mMediaPlayer: MediaPlayer? = null
    private var mAudioManager: AudioManager? = null
    private var mAudioFocusListener: AudioManager.OnAudioFocusChangeListener? = null
    private var mSongBinder: SongBinder? = null

    override fun onCreate() {
        println("---初始化音乐播放服务---onCreate")
        super.onCreate()
        mSongList = mutableListOf()
        mMediaPlayer = MediaPlayer()
        mSongBinder = SongBinder()
        mAudioManager = SongApplication.getApplication()?.getSystemService(Context.AUDIO_SERVICE) as AudioManager?

        mAudioFocusListener = AudioManager.OnAudioFocusChangeListener {
            println("---OnAudioFocusChangeListener---$it")
            if (it == AudioManager.AUDIOFOCUS_LOSS) {
                mSongBinder!!.pause()
            } else if (it == AudioManager.AUDIOFOCUS_GAIN) {
                mSongBinder!!.play(mCurrentSong)
            }
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        println("---初始化音乐播放服务---onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder {
        println("---音乐播放服务绑定---onBind")
        return SongBinder()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        println("---音乐播放服务解綁---onUnbind")
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        println("---音乐播放服务销毁---onDestroy")
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
            println("---SongBinder---initSongList--$songList")
            mSongList = songList
        }

        fun play(song: Song?): Boolean {
            println("---SongBinder---play--$song")
            if (mMediaPlayer != null && song != null) {
                mCurrentPlaySongIndex = getSongIndex(song)
                val songUrl = song.url
                if (!TextUtils.isEmpty(songUrl)) {
                    paueOtherMediaPlayer()

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
            println("---SongBinder---pause--")
            return if (mMediaPlayer != null) {
                if (mMediaPlayer!!.isPlaying) {
                    mMediaPlayer!!.pause()
                }
                mMediaPlayer!!.isPlaying
            } else {
                true
            }
        }

        private fun paueOtherMediaPlayer() {
            if (mAudioManager != null) {
                mAudioManager!!.requestAudioFocus(
                    mAudioFocusListener,
                    AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN
                )
            }
        }


        fun playNext() {
            println("---SongBinder---playNext--")
            mCurrentPlaySongIndex++
            if (mCurrentPlaySongIndex <= mSongList!!.count()) {
                val nextSong = mSongList!![mCurrentPlaySongIndex]
                play(nextSong)
            } else {
                pause()
            }
        }

        private fun getSongIndex(targetSong: Song): Int {
            println("---SongBinder---getSongIndex--$targetSong")
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