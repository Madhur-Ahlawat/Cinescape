package com.cinescape1.ui.main.dailogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import com.cinescape1.databinding.DialogLoaderBinding


class LoaderDialog private constructor(context: Context) : Dialog(context) {
    companion object {
        var binding: DialogLoaderBinding? = null
        var loaderAlertDialog: Dialog? = null
        fun getInstance(context: Context, layoutInflater: LayoutInflater): Dialog? {
            if (loaderAlertDialog != null) {
               if (loaderAlertDialog!!.isShowing){
                    loaderAlertDialog?.dismiss()
                }
                loaderAlertDialog = null
            }
            binding = DialogLoaderBinding.inflate(layoutInflater)
            loaderAlertDialog = Dialog(context, android.R.style.Theme_Translucent_NoTitleBar)
            loaderAlertDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            loaderAlertDialog?.setContentView(binding!!.root)
            val window: Window? = loaderAlertDialog?.window
            val wlp: WindowManager.LayoutParams? = window?.attributes
            wlp?.gravity = Gravity.CENTER
            wlp?.flags = wlp?.flags?.and(WindowManager.LayoutParams.FLAG_BLUR_BEHIND.inv())
            window?.attributes = wlp
            loaderAlertDialog?.window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
            loaderAlertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            loaderAlertDialog!!.setCancelable(false)
            return loaderAlertDialog
        }
    }

}