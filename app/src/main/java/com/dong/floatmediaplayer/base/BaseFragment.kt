package com.dong.floatmediaplayer.base

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment(), View.OnTouchListener {
    protected var mActivity: BaseActivity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = (activity as BaseActivity?)!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.setOnTouchListener(this)
        initView()
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return true
    }

    fun startService(service: Intent): ComponentName? {
        return mActivity?.startService(service)
    }

    fun bindService(
            service: Intent, conn: ServiceConnection,
            flags: Int
    ): Boolean {
        if (mActivity != null) {
            return mActivity!!.bindService(service, conn, flags)
        }
        return false
    }

    fun unbindService(conn: ServiceConnection) {
        mActivity?.unbindService(conn)
    }

    fun startFragment(@IdRes containerViewId: Int, fragment: BaseFragment) {
        mActivity!!.startFragment(containerViewId, fragment)
    }


    abstract fun getLayoutId(): Int

    abstract fun initView()
}