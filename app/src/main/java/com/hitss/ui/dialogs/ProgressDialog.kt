package com.hitss.ui.dialogs

import android.animation.ValueAnimator
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import com.hitss.databinding.ProgressDialogBinding

class ProgressDialog (context: Context): Dialog(context){
    private var myContext: Context =context
    private lateinit var binding: ProgressDialogBinding
    init {
        initDialog()
    }
    private fun initDialog() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.setCancelable(false)
        binding = ProgressDialogBinding.inflate(LayoutInflater.from(myContext))
        this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        this.window?.setDimAmount(0f)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.setContentView(binding.root)
        this.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        initAnimator()
    }

    fun initAnimator() {
        val animator = ValueAnimator.ofInt(0, 3)

        animator.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Int
            binding.textProgress.text = when (value) {
                0 -> "."
                1 -> ".."
                2 -> "..."
                else -> "."
            }
        }

        animator.duration = 1000
        animator.repeatCount = ValueAnimator.INFINITE
        animator.start()
    }
}