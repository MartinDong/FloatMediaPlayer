package com.dong.floatmediaplayer

import com.dong.floatmediaplayer.base.BaseMvpActivity
import com.dong.floatmediaplayer.bean.wangyi.Song
import com.dong.floatmediaplayer.contract.SongHiBaiListContract
import com.dong.floatmediaplayer.presenter.SongHiBaiListPresenter

class SongHiBaiListActivity : BaseMvpActivity<SongHiBaiListPresenter>(), SongHiBaiListContract.View {

    override fun getLayoutId(): Int {
        return R.layout.activity_song_hi_bai_list
    }

    override fun initView() {
        mPresenter = SongHiBaiListPresenter()
        mPresenter!!.attachView(this)
        mPresenter!!.getSongList()
    }

    override fun onPlaySong(song: Song) {
        println("=======onPlaySong========")
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
    }
}
