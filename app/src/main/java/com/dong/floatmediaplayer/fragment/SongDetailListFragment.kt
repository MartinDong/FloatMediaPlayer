package com.dong.floatmediaplayer.fragment

import com.dong.floatmediaplayer.R
import com.dong.floatmediaplayer.base.BaseMvpFragment
import com.dong.floatmediaplayer.bean.wangyi.Song
import com.dong.floatmediaplayer.contract.SongDetailContract
import com.dong.floatmediaplayer.presenter.SongDetailPresenter

class SongDetailListFragment : BaseMvpFragment<SongDetailPresenter>(), SongDetailContract.View {
    override fun getLayoutId(): Int {
        return R.layout.fragment_song_detail
    }

    override fun initView() {
        println("SongDetailListFragment=====initView")
    }

    override fun showLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onError(throwable: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showSongDetail(song: Song) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}