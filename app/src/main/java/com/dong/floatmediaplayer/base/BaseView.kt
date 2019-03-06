package com.dong.floatmediaplayer.base

interface BaseView {

    fun showLoading()

    fun hideLoading()

    fun onError(throwable: Throwable)
}