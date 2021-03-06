package com.dong.floatmediaplayer.base

abstract class BaseMvpActivity<T : BasePresenter<*>> : BaseActivity(), BaseView {
    protected var mPresenter: T? = null

    override fun onDestroy() {
        if (mPresenter != null) {
            mPresenter!!.detachView()
        }
        super.onDestroy()
    }
}