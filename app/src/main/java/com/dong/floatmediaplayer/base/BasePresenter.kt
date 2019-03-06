package com.dong.floatmediaplayer.base

open class BasePresenter<V : BaseView> {
    var mView: V? = null

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