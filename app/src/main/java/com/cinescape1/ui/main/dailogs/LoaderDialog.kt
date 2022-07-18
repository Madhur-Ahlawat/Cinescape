package com.cinescape1.ui.main.dailogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.cinescape1.R
import com.cinescape1.databinding.DialogLoaderBinding

class LoaderDialog(private val message: Int) : DialogFragment() {
    private var binding: DialogLoaderBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_Dialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = false

        return DialogLoaderBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}