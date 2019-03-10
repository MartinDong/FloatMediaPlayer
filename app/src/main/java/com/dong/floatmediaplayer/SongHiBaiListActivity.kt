package com.dong.floatmediaplayer

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.dong.floatmediaplayer.adapter.SongHiBaiListAdapter
import com.dong.floatmediaplayer.base.BaseMvpActivity
import com.dong.floatmediaplayer.bean.wangyi.Song
import com.dong.floatmediaplayer.bean.wangyi.SongListResponse
import com.dong.floatmediaplayer.contract.SongHiBaiListContract
import com.dong.floatmediaplayer.presenter.SongHiBaiListPresenter
import com.dong.floatmediaplayer.service.SongPlayerService
import kotlinx.android.synthetic.main.fragment_song_list.*


class SongHiBaiListActivity : BaseMvpActivity<SongHiBaiListPresenter>(), SongHiBaiListContract.View {


    private lateinit var mSongListAdapter: SongHiBaiListAdapter
    private var mSongList: MutableList<Song> = mutableListOf()
    private var mSongBinder: SongPlayerService.SongBinder? = null
    private var mSongServiceConnection: SongServiceConnection? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_song_list
    }

    @SuppressLint("WrongConstant")
    override fun initView() {
        println("------initView------")
        mSongListAdapter = SongHiBaiListAdapter(mSongList, object : SongHiBaiListAdapter.OperationListener {
            override fun playSong(song: Song) {
                onPlaySong(song)
            }

            override fun pauseSong(song: Song) {
                pauseSong(song)
            }
        })

        val layoutManager = LinearLayoutManager(this, VERTICAL, false)

        rv_song_list.adapter = mSongListAdapter
        rv_song_list.layoutManager = layoutManager

        mPresenter = SongHiBaiListPresenter()
        mPresenter!!.attachView(this)
        mPresenter!!.getSongList()

        mSongServiceConnection = SongServiceConnection()
        val songServiceIntent = Intent(this, SongPlayerService::class.java)
        startService(songServiceIntent)
        bindService(songServiceIntent, mSongServiceConnection!!, BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        println("------onDestroy------")
        super.onDestroy()
        if (mSongServiceConnection != null) {
            unbindService(mSongServiceConnection!!)
        }
    }

    override fun onPlaySong(song: Song) {
        println("------onPlaySong------$song")

        if (mSongBinder != null) {
            mSongBinder!!.play(song)
        }
    }

    override fun pauseSong(song: Song) {
        println("------pauseSong------=$song")
    }

    override fun showLoading() {
        println("------showLoading------")
    }

    override fun hideLoading() {
        print("------hideLoading------")
    }

    override fun onError(throwable: Throwable) {
        println("------onError------")
    }

    override fun onSuccess(message: Any) {
        println("------onSuccess------$message")
        mSongList.addAll((message as SongListResponse).data.songs)
        mSongListAdapter.notifyDataSetChanged()
    }

    inner class SongServiceConnection : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            println("------onServiceDisconnected------$name")
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            println("------onServiceConnected------$name")
            mSongBinder = service as SongPlayerService.SongBinder
            mSongBinder!!.initSongList(mSongList)
        }

    }
}
