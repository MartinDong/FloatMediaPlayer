package com.dong.floatmediaplayer

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import com.dong.floatmediaplayer.base.BaseActivity
import com.dong.floatmediaplayer.bean.wangyi.Song
import com.dong.floatmediaplayer.contract.MainContract
import com.dong.floatmediaplayer.service.SongPlayerService
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), MainContract.View {

    private var mSongBinder: SongPlayerService.SongBinder? = null
    private var mSongServiceConnection: SongServiceConnection? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        mSongServiceConnection = SongServiceConnection()
        val songServiceIntent = Intent(this, SongPlayerService::class.java)
        startService(songServiceIntent)
        bindService(songServiceIntent, mSongServiceConnection!!, AppCompatActivity.BIND_AUTO_CREATE)

        drag_float_view.setOnClickListener {
            if (mSongBinder != null) {
                mSongBinder!!.playNext()
            }
        }
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

    inner class SongServiceConnection : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            println("------onServiceDisconnected------$name")
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            println("------onServiceConnected------$name")
            mSongBinder = service as SongPlayerService.SongBinder
        }
    }
}
