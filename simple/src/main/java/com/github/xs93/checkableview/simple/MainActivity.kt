package com.github.xs93.checkableview.simple

import android.os.Bundle
import com.github.xs93.checkableview.CheckGroup
import com.github.xs93.checkableview.simple.databinding.ActivityMainBinding
import com.github.xs93.core.base.ui.viewbinding.BaseVbActivity
import com.github.xs93.core.utils.toast.ToastUtils

class MainActivity : BaseVbActivity<ActivityMainBinding>(R.layout.activity_main) {
    override fun initView(savedInstanceState: Bundle?) {
        ToastUtils.init(this)
        binding.checkGroup.setOnCheckedChangeListener(object : CheckGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(checkGroup: CheckGroup, checkId: Int) {
                ToastUtils.show(checkId.toString())
            }
        })

        binding.checkableFrameLayout.setOnClickListener {
            binding.checkableFrameLayout.toggle()
        }

        binding.checkableConstraintLayout.setOnClickListener {
            binding.checkableConstraintLayout.toggle()
        }

        binding.checkableLinearLayout.setOnClickListener {
            binding.checkableLinearLayout.toggle()
        }
    }
}