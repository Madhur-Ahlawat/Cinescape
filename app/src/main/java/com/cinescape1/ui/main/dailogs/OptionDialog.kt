package com.cinescape1.ui.main.dailogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.cinescape1.R
import com.cinescape1.databinding.DialogOptionBinding
import com.cinescape1.utils.Constant

class OptionDialog(
    context: Context,
    @DrawableRes private val illustrationRes: Int = -1,
    @StringRes private val title: Int,
    private val subtitle: String,
    @StringRes private val positiveBtnText: Int,
    @StringRes private val negativeBtnText: Int,
    private val positiveClick: (() -> Unit)? = null,
    private val negativeClick: (() -> Unit)? = null
) : Dialog(context, R.style.AppTheme_Dialog) {

    private var binding: DialogOptionBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = DialogOptionBinding.inflate(layoutInflater, null, false)
        setContentView(binding?.root!!)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)

        if (Constant.DISPLAY ==0){
            Constant.DISPLAY =1
            //binding?.subtitle?.gravity = Gravity.START
        }else{
            //binding?.subtitle?.gravity = Gravity.CENTER
        }


        if (positiveBtnText==R.string.ok){
            binding?.negativeBtn?.visibility = View.GONE
        }else{
            binding?.negativeBtn?.visibility = View.VISIBLE
        }

        binding?.illustration?.setImageResource(R.mipmap.ic_launcher)
        binding?.title?.text = Html.fromHtml(context.getString(title))
        binding?.subtitle?.text = Html.fromHtml(subtitle)
        binding?.positiveBtn?.text = context.getString(positiveBtnText)
        binding?.negativeBtn?.text = context.getString(negativeBtnText)

        binding?.positiveBtn?.setOnClickListener {
            positiveClick?.invoke()
            dismiss()
            binding = null
        }
        binding?.negativeBtn?.setOnClickListener {
            negativeClick?.invoke()
            dismiss()
            binding = null
        }
    }

    public override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        binding = null
    }

}