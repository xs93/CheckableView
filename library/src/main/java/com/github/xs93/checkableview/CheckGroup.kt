package com.github.xs93.checkableview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Checkable
import android.widget.CompoundButton
import androidx.annotation.IdRes

/**
 *
 * 可以包含一组checkbox,用于互斥,实现RadioGroup的功能，但是可以随意摆放位置
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/8/29 16:01
 * @email 466911254@qq.com
 */
open class CheckGroup : View {


    private var mIds: IntArray = IntArray(32)
    private var mCount: Int = 0
    private var mReferenceIds: String? = null

    private var mCheckedId = -1
    private var mProtectFromCheckedChange = false
    private var mChildCheckedChangeListener: CompoundButton.OnCheckedChangeListener? = null

    private var mOnCheckedChangeListener: OnCheckedChangeListener? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        mChildCheckedChangeListener = CheckedStateTracker()
        val a = context.obtainStyledAttributes(attrs, R.styleable.CheckGroup)
        val count = a.indexCount
        for (i in 0 until count) {
            val attr = a.getIndex(i)
            if (attr == R.styleable.CheckGroup_ids) {
                mReferenceIds = a.getString(attr)
            } else if (attr == R.styleable.CheckGroup_checkedId) {
                mCheckedId = a.getResourceId(R.styleable.CheckGroup_checkedId, -1)
            }
        }
        a.recycle()
        setIds(mReferenceIds)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setListener()
        if (mCheckedId != -1) {
            mProtectFromCheckedChange = true
            setCheckedStateForView(mCheckedId, true)
            mProtectFromCheckedChange = false
            setCheckedId(mCheckedId)
        }
    }


    private fun setListener() {
        var parent: ViewGroup? = null
        if (this.parent is ViewGroup) {
            parent = this.parent as ViewGroup
        }
        if (parent != null) {
            for (i in 0 until mCount) {
                val id = mIds[i]
                val view = parent.findViewById<View>(id)
                if (view is Checkable) {
                    (view as CheckBox).isChecked = false
                    view.setOnCheckedChangeListener(mChildCheckedChangeListener)
                }
            }
        }
    }

    private fun setCheckedStateForView(viewId: Int, checked: Boolean) {
        val viewParent = parent
        if (viewParent is ViewGroup) {
            val checkedView = viewParent.findViewById<View>(viewId)
            if (checkedView is Checkable) {
                checkedView.isChecked = checked
            }
        }
    }

    private fun setCheckedId(@IdRes id: Int) {
        mCheckedId = id
        mOnCheckedChangeListener?.onCheckedChanged(this, mCheckedId)
    }


    fun setOnCheckedChangeListener(onCheckedChangeListener: OnCheckedChangeListener) {
        mOnCheckedChangeListener = onCheckedChangeListener
    }

    private fun setIds(idList: String?) {
        mReferenceIds = idList
        if (idList != null) {
            var begin = 0
            mCount = 0
            while (true) {
                val end = idList.indexOf(44.toChar(), begin)
                if (end == -1) {
                    addID(idList.substring(begin))
                    return
                }
                addID(idList.substring(begin, end))
                begin = end + 1
            }
        }
    }

    private fun addID(idString: String?) {
        var finalIdString = idString
        if (!finalIdString.isNullOrBlank()) {
            finalIdString = finalIdString.trim { it <= ' ' }
            val resId = findId(finalIdString)
            if (resId != 0) {
                addRscID(resId)
            } else {
                Log.w("CheckGroup", "Could not find id of \"$idString\"")
            }
        }
    }

    private fun addRscID(id: Int) {
        if (id != this.id) {
            if (mCount + 1 > mIds.size) {
                mIds = mIds.copyOf(mIds.size * 2)
            }
            mIds[mCount] = id
            ++mCount
        }
    }

    private fun findId(referenceId: String): Int {
        return resources.getIdentifier(referenceId, "id", context.packageName)
    }

    private inner class CheckedStateTracker : CompoundButton.OnCheckedChangeListener {
        override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
            // prevents from infinite recursion
            if (mProtectFromCheckedChange) {
                return
            }
            mProtectFromCheckedChange = true
            val id = buttonView.id
            if (mCheckedId != -1) {
                setCheckedStateForView(mCheckedId, mCheckedId == id)
            }
            mProtectFromCheckedChange = false
            if (mCheckedId != id) {
                setCheckedId(id)
            }
        }
    }


    interface OnCheckedChangeListener {
        fun onCheckedChanged(checkGroup: CheckGroup, @IdRes checkId: Int)
    }
}