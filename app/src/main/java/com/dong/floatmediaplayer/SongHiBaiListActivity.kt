package com.dong.floatmediaplayer

import com.dong.floatmediaplayer.base.BaseMvpActivity
import com.dong.floatmediaplayer.bean.SongHiBaiBody
import com.dong.floatmediaplayer.contract.SongHiBaiListContract
import com.dong.floatmediaplayer.presenter.SongHiBaiListPresenter

class SongHiBaiListActivity : BaseMvpActivity<SongHiBaiListPresenter>(), SongHiBaiListContract.View {
    override fun onPlaySong(song: SongHiBaiBody) {
        print("=======onPlaySong========")
    }

    override fun showLoading() {
        print("=======showLoading========")
    }

    override fun hideLoading() {
        print("=======hideLoading========")
    }

    override fun onError(throwable: Throwable) {
        print("=======onError========")
    }

    override fun onSuccess(message: Any) {
        print("=======onSuccess========$message")
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_song_hi_bai_list
    }

    override fun initView() {
        mPresenter = SongHiBaiListPresenter()
        mPresenter!!.attachView(this)
        mPresenter!!.getSongList()
    }

}
