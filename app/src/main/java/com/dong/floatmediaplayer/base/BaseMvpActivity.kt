package com.dong.floatmediaplayer.base

import android.os.Bundle

abstract class BaseMvpActivity<T : BasePresenter<*>> : BaseActivity(), BaseView {
    protected var mPresenter: T? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        if (mPresenter != null) {
            mPresenter!!.detachView()
        }
        super.onDestroy()
    }
}