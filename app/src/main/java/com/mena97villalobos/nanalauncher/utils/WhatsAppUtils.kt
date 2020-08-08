package com.mena97villalobos.nanalauncher.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity


class WhatsAppUtils(
    private val contactId: String,
    private val context: Context
) {

    fun voiceCall() {
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.setDataAndType(
            Uri.parse("content://com.android.contacts/data/$contactId"),
            "vnd.android.cursor.item/vnd.com.whatsapp.video.call"
        )
        intent.setPackage("com.whatsapp")
        context.startActivity(intent)
    }

}