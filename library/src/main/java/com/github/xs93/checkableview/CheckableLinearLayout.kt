package com.github.xs93.checkableview

import android.content.Context
import android.util.AttributeSet
import android.widget.Checkable
import android.widget.LinearLayout

/**
 * 包含Check状态的 LinearLayout
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/12/27 13:59
 * @email 466911254@qq.com
 */
class CheckableLinearLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), Checkable {

    companion object {
        private val ATTR_CHECKED_STATE = intArrayOf(android.R.attr.state_checked)
    }

    private var checked = false
    private var mBroadcasting = false
    private var mOnCheckedChangeListener: OnCheckedChangeListener? = null

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

    fun setOnCheckedChangeListener(listener: OnCheckedChangeListener?) {
        this.mOnCheckedChangeListener = listener
    }

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.CheckableLinearLayout)
        checked = ta.getBoolean(R.styleable.CheckableLinearLayout_android_checked, false)
        ta.recycle()
    }

    interface OnCheckedChangeListener {
        /**
         * Called when the checked state of a compound button has changed.
         *
         * @param view      The Checkable ImageView  whose state has changed.
         * @param isChecked The new checked state of buttonView.
         */
        fun onCheckedChanged(view: CheckableLinearLayout, isChecked: Boolean)
    }
}