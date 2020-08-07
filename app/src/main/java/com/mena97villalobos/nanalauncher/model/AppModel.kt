package com.mena97villalobos.nanalauncher.model

import android.graphics.drawable.Drawable

data class AppModel (val appName: CharSequence, val appIcon: Drawable, val onClickListener: AppClickListener)

class AppClickListener(val clickListener: () -> Unit) {

    fun onClick() = clickListener()

}