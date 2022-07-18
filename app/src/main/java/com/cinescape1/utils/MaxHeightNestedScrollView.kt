package com.cinescape1.utils

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.core.widget.NestedScrollView
import com.cinescape1.R


class MaxHeightNestedScrollView(
    context: Context,
    @Nullable attrs: AttributeSet?,
    defStyleAttr: Int
) :
    NestedScrollView(context, attrs, defStyleAttr) {
    var maxHeight = -1

    constructor(context: Context) : this(context, null, 0) // Modified changes
    {
    }

    constructor(context: Context, @Nullable attrs: AttributeSet?) : this(
        context,
        attrs,
        0
    ) // Modified changes
    {
    }

    // Modified changes
    private fun init(context: Context, @Nullable attrs: AttributeSet?, defStyleAttr: Int) {
        val a: TypedArray = context.obtainStyledAttributes(
            attrs, R.styleable.MaxHeightScrollView, defStyleAttr, 0
        )
        maxHeight = a.getDimensionPixelSize(R.styleable.MaxHeightScrollView_maxHeight, 0)
        a.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpec = heightMeasureSpec
        if (maxHeight > 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST)
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    init {
        init(context, attrs, defStyleAttr) // Modified changes
    }
}