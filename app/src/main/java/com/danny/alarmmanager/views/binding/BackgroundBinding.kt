package com.danny.alarmmanager.views.binding

import android.graphics.Color
import android.view.View
import androidx.databinding.BindingAdapter

object BackgroundBinding {
    @JvmStatic
    @BindingAdapter("android:color_hex")
    fun colorHex(view: View, hex: String) {
        view.background.setTint(Color.parseColor(hex))
    }
}