package com.cinescape1.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

fun Context?.toast(@SuppressLint("SupportAnnotationUsage") @StringRes resId: String?){
    resId ?: return
    this ?: return
    Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
}

fun Fragment.toast(@SuppressLint("SupportAnnotationUsage") @StringRes resId: String?){
    requireContext().toast(resId)
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

