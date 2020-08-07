package com.mena97villalobos.nanalauncher.utils

import android.content.Context
import android.hardware.camera2.CameraManager

class FlashlightUtils(private val context: Context) {

    val state = false;

    private fun changeFlashlightState() {
        val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager?
        cameraManager?.cameraIdList?.get(0)?.let {
            cameraManager.setTorchMode(it, state)
        }
    }

}