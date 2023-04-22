package com.github.xs93.checkableview

import android.content.Context
import android.util.AttributeSet
import android.widget.Checkable
import com.google.android.material.imageview.ShapeableImageView

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/8/4 14:36
 * @email 466911254@qq.com
 */
@Suppress("unused")
class CheckableImageView : ShapeableImageView, Checkable {

    companion object {
        private val ATTR_CHECKED_STATE = intArrayOf(android.R.attr.state_checked)
    }

    private var checked = false
    private var mBroadcasting = false
    private var mOnCheckedChangeListener: OnCheckedChangeListener? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.CheckableImageView)
        checked = ta.getBoolean(R.styleable.CheckableConstraintLayout_android_checked, false)
        ta.recycle()
    }

    override fun setChecked(checked: Boolean) {
        if (this.checked != checked) {
            this.checked = checked
            refreshDrawableState()
            if (mBroadcasting) {
                return
            }
            mBroadcasting = true
            mOnCheckedChangeListener?.onCheckedChanged(this, this.checked)
            mBroadcasting = false
        }
    }

    override fun isChecked(): Boolean {
        return checked
    }

    override fun toggle() {
        isChecked = !checked
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        if (isChecked) {
            return mergeDrawableStates(super.onCreateDrawableState(extraSpace + 1), ATTR_CHECKED_STATE)
        }
        return super.onCreateDrawableState(extraSpace)
    }

    fun setOnCheckedChangeListener(listener: OnCheckedChangeListener) {
        this.mOnCheckedChangeListener = listener
    }

    interface OnCheckedChangeListener {
        /**
         * Called when the checked state of a compound button has changed.
         *
         * @param view      The Checkable ImageView  whose state has changed.
         * @param isChecked The new checked state of buttonView.
         */
        fun onCheckedChanged(view: CheckableImageView, isChecked: Boolean)
    }
}