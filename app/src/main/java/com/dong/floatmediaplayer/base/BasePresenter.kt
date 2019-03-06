package com.dong.floatmediaplayer.base

interface BasePresenter<V : BaseView> {
    var mView: V?

    fun attachView(view: V) {
        mView = view
    }

    fun detachView() {
        mView = null
    }

    fun isViewAttached(): Boolean {
        return mView != null
    }
}