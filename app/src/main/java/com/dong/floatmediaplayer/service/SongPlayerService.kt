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

    private var mMediaStatusChangeListener: MediaStatusChangeListener? = null

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
        mMediaStatusChangeListener = null
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

        fun setMediaStatusChangeListener(listener: MediaStatusChangeListener) {
            mMediaStatusChangeListener = listener
        }

        fun play(song: Song?): Boolean {
            println("---SongBinder---play--$song")
            mMediaStatusChangeListener?.onPlay(song)
            mCurrentSong = song
            if (mMediaPlayer != null && mCurrentSong != null) {
                mIsPlaying = true
                mCurrentPlaySongIndex = getSongIndex(mCurrentSong!!)
                val songUrl = mCurrentSong!!.url
                if (!TextUtils.isEmpty(songUrl)) {
                    pauseOtherMediaPlayer()

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
            mMediaStatusChangeListener?.onPause(mCurrentSong)
            mIsPlaying = false
            return if (mMediaPlayer != null) {
                if (mMediaPlayer!!.isPlaying) {
                    mMediaPlayer!!.pause()
                }
                mMediaPlayer!!.isPlaying
            } else {
                true
            }
        }

        private fun pauseOtherMediaPlayer() {
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
            mMediaStatusChangeListener?.onContinue(mCurrentSong)
            return if (mMediaPlayer != null) {
                if (mMediaPlayer!!.isPlaying) {
                    mMediaPlayer!!.start()
                }
                mMediaPlayer!!.isPlaying
            } else {
                true
            }
        }

        fun playNext() {
            println("---SongBinder---playNext--")
            mCurrentPlaySongIndex++
            if (mCurrentPlaySongIndex > 0 && mCurrentPlaySongIndex <= mSongList!!.count()) {
                val nextSong = mSongList!![mCurrentPlaySongIndex]
                play(nextSong)
                mMediaStatusChangeListener?.playNext(nextSong)
            } else {
                pause()
            }
        }

        fun playPrevious() {
            println("---SongBinder---playPrevious--")
            mCurrentPlaySongIndex--
            if (mCurrentPlaySongIndex > 0 && mCurrentPlaySongIndex <= mSongList!!.count()) {
                val nextSong = mSongList!![mCurrentPlaySongIndex]
                play(nextSong)
                mMediaStatusChangeListener?.playPrevious(nextSong)
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
        fun playNext(song: Song?)
        fun playPrevious(song: Song?)
        fun onContinue(song: Song?)
        fun onProgress(totalDuration: Int, currentDuration: Int)
        fun onError(exception: Exception)
    }
}