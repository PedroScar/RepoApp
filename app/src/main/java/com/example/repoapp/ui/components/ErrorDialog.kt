package com.example.repoapp.ui.components

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.Window
import com.example.repoapp.databinding.ErrorDialogBinding

class ErrorDialog(context: Context, retry: () -> Unit) {

    private val dialog = Dialog(context)
    private val binding = ErrorDialogBinding.inflate(LayoutInflater.from(context))

    init {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)

        binding.bClose.setOnClickListener { dialog.dismiss() }
        binding.bRetry.setOnClickListener {
            dialog.dismiss()
            retry.invoke()
        }

        dialog.setContentView(binding.root)
    }

    fun showDialog() = dialog.show()
}