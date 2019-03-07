package com.dong.floatmediaplayer

import android.annotation.SuppressLint
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.dong.floatmediaplayer.adapter.SongHiBaiListAdapter
import com.dong.floatmediaplayer.base.BaseMvpActivity
import com.dong.floatmediaplayer.bean.wangyi.Song
import com.dong.floatmediaplayer.bean.wangyi.SongListResponse
import com.dong.floatmediaplayer.contract.SongHiBaiListContract
import com.dong.floatmediaplayer.presenter.SongHiBaiListPresenter
import kotlinx.android.synthetic.main.activity_song_hi_bai_list.*
import android.media.MediaPlayer


class SongHiBaiListActivity : BaseMvpActivity<SongHiBaiListPresenter>(), SongHiBaiListContract.View {


    lateinit var mSongListAdapter: SongHiBaiListAdapter
    private var mSongList: MutableList<Song> = mutableListOf()
    private val mMediaPlayer = MediaPlayer()

    override fun getLayoutId(): Int {
        return R.layout.activity_song_hi_bai_list
    }

    @SuppressLint("WrongConstant")
    override fun initView() {
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
    }

    override fun onPlaySong(song: Song) {
        println("=======onPlaySong========$song")
        if (mMediaPlayer.isPlaying) {
            mMediaPlayer.reset()
        }

        mMediaPlayer.setDataSource(song.url)
        mMediaPlayer.prepare()
        mMediaPlayer.start()
    }

    override fun pauseSong(song: Song) {
        println("=======pauseSong========$song")
    }

    override fun showLoading() {
        println("=======showLoading========")
    }

    override fun hideLoading() {
        print("=======hideLoading========")
    }

    override fun onError(throwable: Throwable) {
        println("=======onError========")
    }

    override fun onSuccess(message: Any) {
        println("=======onSuccess========$message")
        mSongList.addAll((message as SongListResponse).data.songs)
        mSongListAdapter.notifyDataSetChanged()
    }
}
