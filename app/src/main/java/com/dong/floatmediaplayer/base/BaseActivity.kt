package com.dong.floatmediaplayer.base

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initView()
    }

    abstract fun getLayoutId(): Int

    abstract fun initView()

    fun startFragment(@IdRes containerViewId: Int, fragment: BaseFragment) {
        val transaction = supportFragmentManager.beginTransaction()
        //设置专场动画
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.replace(containerViewId, fragment)
        transaction.addToBackStack(fragment.javaClass.name)
        transaction.commit()
    }
}