package com.example.repoapp.ui

import android.view.View

fun View.changeVisibleGone(show: Boolean) {
    this.visibility = if (show) View.VISIBLE else View.GONE
}