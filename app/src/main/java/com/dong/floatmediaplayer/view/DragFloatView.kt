package com.dong.floatmediaplayer.view

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator

class DragFloatView : View {

    private var parentHeight: Int = 0
    private var parentWidth: Int = 0

    private var lastX: Int = 0
    private var lastY: Int = 0

    private var isDrag: Boolean = false

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        println("====Init View====")
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        println("onTouchEvent====$event")

        val rawX = event.rawX.toInt()
        val rawY = event.rawY.toInt()

        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                isPressed = true
                isDrag = false
                //让父控件不进行事件拦截
                parent.requestDisallowInterceptTouchEvent(true)
                lastX = rawX
                lastY = rawY

                val parent: ViewGroup
                if (getParent() != null) {
                    parent = getParent() as ViewGroup
                    parentHeight = parent.height
                    parentWidth = parent.width
                }
            }
            MotionEvent.ACTION_MOVE -> {
                isDrag = !(parentWidth <= 0 || parentHeight <= 0)
                val dx = rawX - lastX
                val dy = rawY - lastY

                //修复华为手机无法点击的bug
                val distance = Math.sqrt((dx * dx + dy * dy).toDouble()).toInt()
                if (distance == 0) {
                    isDrag = false
                } else {
                    var x = x + dx
                    var y = y + dy

                    //检测边界，上下左右
                    x = if (x < 0) 0F else if (x > parentWidth - width) (parentWidth - width).toFloat() else x
                    y = if (y < 0) 0F else if (getY() + height > parentHeight) (parentHeight - height).toFloat() else y

                    setX(x)
                    setY(y)

                    lastX = rawX
                    lastY = rawY

                    println("isDrag===$isDrag===lastX===$lastX===lastY===$lastY===parentWidth===$parentWidth===parentHeight===$parentHeight")
                }
            }

            MotionEvent.ACTION_UP -> if (!isNotDrag()) {
                isPressed = false

                if (rawX >= parentWidth / 2) {
                    //靠右吸附
                    animate()
                        .setInterpolator(DecelerateInterpolator())
                        .setDuration(500)
                        .xBy(parentWidth - width - x)
                        .start()
                } else {
                    //靠左吸附
                    val oa = ObjectAnimator.ofFloat(this, "x", 0F)
                    oa.interpolator = DecelerateInterpolator()
                    oa.duration = 500
                    oa.start()
                }
            }
        }

        return !isNotDrag() || super.onTouchEvent(event)
    }

    private fun isNotDrag(): Boolean {
        return !isDrag && (x == 0f || x == (parentWidth - width).toFloat())
    }


}