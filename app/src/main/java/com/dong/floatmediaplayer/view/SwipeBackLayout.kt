package com.dong.floatmediaplayer.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.customview.widget.ViewDragHelper
import com.dong.floatmediaplayer.base.BaseFragment


class SwipeBackLayout : FrameLayout {
    /**
     * Edge flag indicating that the left edge should be affected.
     */
    val EDGE_LEFT = ViewDragHelper.EDGE_LEFT

    /**
     * Edge flag indicating that the right edge should be affected.
     */
    val EDGE_RIGHT = ViewDragHelper.EDGE_RIGHT

    /**
     * Edge flag indicating that the top edge should be affected.
     */
    val EDGE_TOP = ViewDragHelper.EDGE_TOP

    /**
     * Edge flag indicating that the bottom edge should be affected.
     */
    val EDGE_BOTTOM = ViewDragHelper.EDGE_BOTTOM

    /**
     * Edge flag indicating that the all edge should be affected.
     */
    val EDGE_ALL = EDGE_LEFT or EDGE_RIGHT or EDGE_TOP or EDGE_BOTTOM

    /**
     * A view is not currently being dragged or animating as a result of a
     * fling/snap.
     */
    val STATE_IDLE = ViewDragHelper.STATE_IDLE

    /**
     * A view is currently being dragged. The position is currently changing as
     * a result of user input or simulated user input.
     */
    val STATE_DRAGGING = ViewDragHelper.STATE_DRAGGING

    /**
     * A view is currently settling into place as a result of a fling or
     * predefined non-interactive motion.
     */
    val STATE_SETTLING = ViewDragHelper.STATE_SETTLING

    var mFragment: BaseFragment? = null
    var mContentView: View? = null


    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        println("====Init View====")
    }

    fun attachToFragment(fragment: BaseFragment, view: View) {
        addView(view)
        setFragment(fragment, view)
    }

    fun setFragment(fragment: BaseFragment, view: View) {
        mFragment = fragment
        mContentView = view
    }


}