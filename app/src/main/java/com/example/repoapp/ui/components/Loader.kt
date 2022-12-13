package com.example.repoapp.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.repoapp.databinding.ComponentIconTextBinding
import com.example.repoapp.databinding.LoaderBinding

class Loader @JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = LoaderBinding.inflate(LayoutInflater.from(context), this, true)

}
