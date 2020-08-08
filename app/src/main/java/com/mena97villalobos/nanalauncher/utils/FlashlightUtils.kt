package com.mena97villalobos.nanalauncher.utils

import android.content.Context
import android.hardware.camera2.CameraManager
import android.widget.TextView
import com.mena97villalobos.nanalauncher.R

class FlashlightUtils(private val context: Context) {

    private var state = false

    fun changeFlashlightState(flashlightView: TextView) {
        val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager?
        cameraManager?.cameraIdList?.get(0)?.let {
            cameraManager.setTorchMode(it, state)
            state = !state
            flashlightView.text = context.getText(
                if (state) R.string.flashlight_on
                else R.string.flashlight_off
            )
        }
    }

}