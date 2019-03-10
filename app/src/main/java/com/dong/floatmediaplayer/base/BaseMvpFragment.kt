package com.dong.floatmediaplayer.base

abstract class BaseMvpFragment<T : BasePresenter<*>> : BaseFragment(), BaseView {
    protected var mPresenter: T? = null

    override fun onDetach() {
        if (mPresenter != null) {
            mPresenter!!.detachView()
        }
        super.onDetach()
    }
}