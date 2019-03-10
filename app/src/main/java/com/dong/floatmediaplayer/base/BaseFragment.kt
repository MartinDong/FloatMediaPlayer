package com.dong.floatmediaplayer.base

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

abstract class BaseFragment : Fragment() {
    protected var mActivity: FragmentActivity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = activity!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
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

    abstract fun getLayoutId(): Int

    abstract fun initView()
}