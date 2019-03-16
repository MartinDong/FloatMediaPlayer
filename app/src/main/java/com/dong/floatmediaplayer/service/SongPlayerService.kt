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
    private var mCurrentPlaySongIndex = -1

    private var mMediaPlayer: MediaPlayer? = null
    private var mAudioManager: AudioManager? = null
    private var mAudioFocusListener: AudioManager.OnAudioFocusChangeListener? = null
    private var mSongBinder: SongBinder? = null

    private var mIsPlaying = false

    private var mStatusListeners: MutableList<MediaStatusChangeListener>? = null

    override fun onCreate() {
        println("---初始化音乐播放服务---onCreate")
        super.onCreate()
        mSongList = mutableListOf()
        mMediaPlayer = MediaPlayer()
        mStatusListeners = mutableListOf()
        mSongBinder = SongBinder()
        mAudioManager = SongApplication.getApplication()?.getSystemService(Context.AUDIO_SERVICE) as AudioManager?

        mAudioFocusListener = AudioManager.OnAudioFocusChangeListener {
            println("---OnAudioFocusChangeListener---$it")
            if (it != AudioManager.AUDIOFOCUS_GAIN) {
                mSongBinder!!.pauseWithLossFocus()
            }
//            else {
//                mSongBinder!!.onContinue()
//            }
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

        fun addMediaStatusChangeListener(listener: MediaStatusChangeListener?) {
            if (listener != null)
                mStatusListeners?.add(listener)
        }

        fun removeMediaStatusChangeListener(listener: MediaStatusChangeListener?) {
            if (listener != null)
                mStatusListeners?.remove(listener)
        }

        fun clearMediaStatusChangeListener() {
            mStatusListeners?.clear()
        }

        fun statusOnPlay(song: Song?) {
            mStatusListeners?.forEach {
                it.onPlay(song)
            }
        }

        fun statusOnPause(song: Song?) {
            mStatusListeners?.forEach {
                it.onPause(song)
            }
        }

        fun statusOnPlayNext(song: Song?) {
            mStatusListeners?.forEach {
                it.onPlayNext(song)
            }
        }

        fun statusOnPlayPrevious(song: Song?) {
            mStatusListeners?.forEach {
                it.onPlayPrevious(song)
            }
        }

        fun statusOnContinue(song: Song?) {
            mStatusListeners?.forEach {
                it.onContinue(song)
            }
        }

        fun statusOnProgress(totalDuration: Int, currentDuration: Int) {
            mStatusListeners?.forEach {
                it.onProgress(totalDuration, currentDuration)
            }
        }

        fun statusOnError(exception: Exception) {
            mStatusListeners?.forEach {
                it.onError(exception)
            }
        }

        fun play(song: Song?): Boolean {
            println("---SongBinder---play--$song")
            mCurrentSong = song
            if (mMediaPlayer != null && mCurrentSong != null) {
                mIsPlaying = true
                mCurrentPlaySongIndex = getSongIndex(mCurrentSong!!)
                val songUrl = mCurrentSong!!.url
                if (!TextUtils.isEmpty(songUrl)) {
                    requestAudioFocus()

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
                    statusOnPlay(song)
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
            mIsPlaying = false
            statusOnPause(mCurrentSong)
            return if (mMediaPlayer != null) {
                if (mMediaPlayer!!.isPlaying) {
                    mMediaPlayer!!.pause()
                }
                mMediaPlayer!!.isPlaying
            } else {
                true
            }
        }

        fun pauseWithLossFocus(): Boolean {
            println("---SongBinder---pauseWithLossFocus--")
            statusOnPause(mCurrentSong)
            return if (mMediaPlayer != null) {
                if (mMediaPlayer!!.isPlaying) {
                    mMediaPlayer!!.pause()
                }
                mMediaPlayer!!.isPlaying
            } else {
                true
            }
        }

        private fun requestAudioFocus() {
            if (mAudioManager != null) {
                mAudioManager!!.requestAudioFocus(
                    mAudioFocusListener,
                    AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN
                )
            }
        }

        fun onContinue(): Boolean {
            println("---SongBinder---onContinue--")
            statusOnContinue(mCurrentSong)
            return if (mMediaPlayer != null) {
                if (!mMediaPlayer!!.isPlaying && isPlaying()) {
                    mMediaPlayer!!.start()
                }
                mMediaPlayer!!.isPlaying
            } else {
                true
            }
        }

        fun playNext() {
            println("---SongBinder---onPlayNext--")
            mCurrentPlaySongIndex++
            if (mCurrentPlaySongIndex > 0 && mCurrentPlaySongIndex <= mSongList!!.count()) {
                val nextSong = mSongList!![mCurrentPlaySongIndex]
                play(nextSong)
                statusOnPlayNext(nextSong)
            } else {
                pause()
            }
        }

        fun playPrevious() {
            println("---SongBinder---onPlayPrevious--")
            mCurrentPlaySongIndex--
            if (mCurrentPlaySongIndex > 0 && mCurrentPlaySongIndex <= mSongList!!.count()) {
                val nextSong = mSongList!![mCurrentPlaySongIndex]
                play(nextSong)
                statusOnPlayPrevious(nextSong)
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

        fun setCurrentSong(song: Song?) {
            mCurrentSong = song
        }

        fun getCurrentSong(): Song? {
            return mCurrentSong
        }

        fun isPlaying(): Boolean {
            return mIsPlaying
        }
    }

    interface MediaStatusChangeListener {
        fun onPlay(song: Song?)
        fun onPause(song: Song?)
        fun onPlayNext(song: Song?)
        fun onPlayPrevious(song: Song?)
        fun onContinue(song: Song?)
        fun onProgress(totalDuration: Int, currentDuration: Int)
        fun onError(exception: Exception)
    }
}