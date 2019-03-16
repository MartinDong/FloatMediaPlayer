package com.dong.floatmediaplayer.fragment

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dong.floatmediaplayer.R
import com.dong.floatmediaplayer.base.BaseMvpFragment
import com.dong.floatmediaplayer.bean.wangyi.Song
import com.dong.floatmediaplayer.contract.SongDetailContract
import com.dong.floatmediaplayer.presenter.SongDetailPresenter
import com.dong.floatmediaplayer.service.SongPlayerService
import kotlinx.android.synthetic.main.fragment_song_detail.*

class SongDetailFragment : BaseMvpFragment<SongDetailPresenter>(), SongDetailContract.View {

    private lateinit var mSong: Song
    private var mCurrentPlaySong: Song? = null
    private var mSongBinder: SongPlayerService.SongBinder? = null
    private var mSongServiceConnection: SongServiceConnection? = null

    private var mMediaStatusChangeListener: SongPlayerService.MediaStatusChangeListener? = null

    fun setSong(song: Song) {
        mSong = song
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_song_detail
    }

    override fun initView() {
        println("SongDetailFragment=====initView")
        mSongServiceConnection = SongServiceConnection()
        val songServiceIntent = Intent(context, SongPlayerService::class.java)
        bindService(songServiceIntent, mSongServiceConnection!!, AppCompatActivity.BIND_AUTO_CREATE)

        showSongDetail(mSong)
    }

    override fun onDetach() {
        println("------onDetach------")
        if (mSongServiceConnection != null) {
            unbindService(mSongServiceConnection!!)
        }
        mSongBinder?.removeMediaStatusChangeListener(mMediaStatusChangeListener)
        super.onDetach()
    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    override fun onError(throwable: Throwable) {

    }

    override fun showSongDetail(song: Song) {
        Glide.with(this)
            .load(song.pic)
            .centerCrop()
            .into(iv_song_pic)

        tv_song_name.text = song.name
        tv_song_singer.text = song.singer

        rl_song_operation.setOnClickListener {
            if (mSongBinder != null) {
                if (mSongBinder!!.isPlaying()) {
                    pauseSong(song)
                } else {
                    onPlaySong(song)
                }
            } else {
                Toast.makeText(activity, "加载音乐出现问题，请稍候重试", Toast.LENGTH_LONG).show()
            }
        }

        iv_operation_previous.setOnClickListener {
            playPrevious()
        }

        iv_operation_next.setOnClickListener {
            playNext()
        }
    }

    private fun initSongStatus(song: Song?) {
        if (mSongBinder != null && song != null) {
            if (mSongBinder!!.isPlaying()) {
                println("------initSongStatus--当前处于播放----$song")

                Glide.with(this)
                    .load(R.drawable.widget_pause_selector)
                    .centerCrop()
                    .into(iv_operation)
            } else {
                println("------initSongStatus--当前处于暂停----$song")

                Glide.with(this)
                    .load(R.drawable.widget_play_selector)
                    .centerCrop()
                    .into(iv_operation)
            }
        }
    }

    override fun onPlaySong(song: Song) {
        if (mSongBinder != null) {
            mSongBinder!!.play(song)
        }
    }

    override fun pauseSong(song: Song) {
        if (mSongBinder != null) {
            mSongBinder!!.pause()
        }
    }

    override fun playNext() {
        if (mSongBinder != null) {
            mSongBinder!!.playNext()
        }
    }

    override fun playPrevious() {
        if (mSongBinder != null) {
            mSongBinder!!.playPrevious()
        }
    }

    inner class SongServiceConnection : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            println("------onServiceDisconnected------$name")
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            println("------onServiceConnected------$name")
            mSongBinder = service as SongPlayerService.SongBinder
            mSongBinder!!.setCurrentSong(mSong)

            mMediaStatusChangeListener = object : SongPlayerService.MediaStatusChangeListener {
                override fun onPlay(song: Song?) {
                    mCurrentPlaySong = song
                    initSongStatus(song)
                    showSongDetail(song!!)
                }

                override fun onPause(song: Song?) {
                    initSongStatus(song)
                }

                override fun onPlayNext(song: Song?) {

                }

                override fun onPlayPrevious(song: Song?) {

                }

                override fun onContinuePlay(song: Song?) {
                }

                override fun onProgress(totalDuration: Int, currentDuration: Int) {

                }

                override fun onError(exception: Exception) {

                }
            }

            mSongBinder!!.addMediaStatusChangeListener(mMediaStatusChangeListener)
            mCurrentPlaySong = mSongBinder!!.getCurrentSong()
            initSongStatus(mCurrentPlaySong)
        }
    }

}