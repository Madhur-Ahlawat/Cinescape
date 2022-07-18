package com.cinescape1.utils

import android.graphics.Color
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View

open class MySpannable(isUnderline: Boolean) : ClickableSpan() {
    private var isUnderline = true
    override fun onClick(widget: View) {}
    override fun updateDrawState(ds: TextPaint) {
        ds.isUnderlineText = isUnderline
        ds.color = Color.parseColor("#ffcb07")
    }

    /**
     * Constructor
     */
    init {
        this.isUnderline = isUnderline
    }
}
