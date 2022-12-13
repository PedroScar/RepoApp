package com.example.repoapp.ui.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.content.res.ResourcesCompat
import com.example.repoapp.R
import com.example.repoapp.databinding.ComponentIconTextBinding

class IconText @JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    val binding = ComponentIconTextBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        if (attrs != null) {
            val atributes = context.theme.obtainStyledAttributes(attrs, R.styleable.IconText, 0, 0)
            val iconType = atributes.getInt(R.styleable.IconText_iconType, 0)

            this.setupIcon(iconType)
        }
    }

    private fun setupIcon(iconType: Int) {
        when (iconType) {
            0 -> {
                binding.ivIcon.setImageDrawable(getDrawable(context, R.drawable.ic_git_star))
                binding.tvLabel.text = context.getString(R.string.stars)
            }
            1 -> {
                binding.ivIcon.setImageDrawable(getDrawable(context, R.drawable.ic_git_fork))
                binding.tvLabel.text = context.getString(R.string.forks)
            }
        }
    }

    fun setCount(count: Int) {
        binding.tvCount.text = count.toString()
    }
}