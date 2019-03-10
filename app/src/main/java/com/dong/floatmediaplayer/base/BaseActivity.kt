package com.dong.floatmediaplayer.base

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import com.dong.floatmediaplayer.R

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
        transaction.setCustomAnimations(
            R.anim.in_from_right,
            R.anim.in_from_right,
            R.anim.in_from_right,
            R.anim.out_to_left
        )
        transaction.replace(containerViewId, fragment)
        transaction.addToBackStack(fragment.javaClass.name)
        transaction.commit()
    }
}