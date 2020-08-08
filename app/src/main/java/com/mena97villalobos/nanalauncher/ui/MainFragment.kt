package com.mena97villalobos.nanalauncher.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.mena97villalobos.nanalauncher.R
import com.mena97villalobos.nanalauncher.ui.adapter.AppsAdapter
import com.mena97villalobos.nanalauncher.databinding.FragmentMainBinding
import com.mena97villalobos.nanalauncher.model.AppClickListener
import com.mena97villalobos.nanalauncher.model.AppModel
import com.mena97villalobos.nanalauncher.model.WhatsAppContact
import com.mena97villalobos.nanalauncher.utils.FlashlightUtils
import com.mena97villalobos.nanalauncher.utils.WhatsAppUtils

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var flashlightUtility: FlashlightUtils

    companion object {
        val allowedApps = listOf(
            "com.google.android.apps.photos",
            "com.google.android.dialer",
            "com.whatsapp",
            "com.android.calculator2",
            "com.android.camera2",
            "com.google.android.apps.messaging",
            "com.android.contacts"
        )

        val whatsAppContacts = listOf(
            WhatsAppContact("Génesis", "3543"),
            WhatsAppContact("Haydi", "387"),
            WhatsAppContact("Jossymar", "388")
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_main,
            container,
            false
        )
        flashlightUtility = FlashlightUtils(requireContext())

        setupView()
        getWhatsAppContacts()
        return binding.root
    }

    private fun setupView() {
        val adapter = AppsAdapter(requireContext())
        adapter.submitList(getSystemAppList())
        binding.appsList.adapter = adapter

        binding.flashlight.setOnClickListener {
            flashlightUtility.changeFlashlightState(it as TextView)
        }
    }

    private fun getSystemAppList() : List<AppModel> {
        val pm: PackageManager = requireContext().packageManager
        val appsList = arrayListOf<AppModel>()

        val i = Intent(Intent.ACTION_MAIN, null)
        i.addCategory(Intent.CATEGORY_LAUNCHER)

        pm.queryIntentActivities(i, 0)
            .filter { resolveInfo -> resolveInfo.activityInfo.packageName in allowedApps }
            .forEach{ri ->
                appsList.add(
                    AppModel(
                        ri.loadLabel(pm),
                        ri.activityInfo.loadIcon(pm),
                        AppClickListener {
                            val launchIntent: Intent? = requireContext().packageManager
                                .getLaunchIntentForPackage(ri.activityInfo.packageName)
                            startActivity(launchIntent)
                        }
                    )
                )
            }
        return appsList.also { it.addAll(getWhatsAppVideoCallsAppModel()) }
    }

    private fun getWhatsAppVideoCallsAppModel() : List<AppModel> {
        val appsList = arrayListOf<AppModel>()
        whatsAppContacts.forEach {
            appsList.add(
                AppModel(
                    getString(R.string.whatsapp_video_call, it.displayName),
                    resources.getDrawable(R.drawable.ic_whatsapp, null),
                    AppClickListener {
                        WhatsAppUtils(it.contactId, requireContext()).voiceCall()
                    }
                )
            )
        }
        return appsList
    }

    private fun getWhatsAppContacts() {
        // here is how to make a projection. you have to use an array. My example only returns the ID, Name of Contact and Mimetype.

        val projection = arrayOf(ContactsContract.Data._ID, ContactsContract.Data.DISPLAY_NAME, ContactsContract.Data.MIMETYPE)
        val cursor = requireContext().contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            projection, null, null,
            ContactsContract.Contacts.DISPLAY_NAME);
        cursor?.let {
            val contactsVideo = arrayListOf<String>()
            val contactsVOIP = arrayListOf<String>()
            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndex(ContactsContract.Data._ID))
                val displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME))
                when (cursor.getString(cursor.getColumnIndex(ContactsContract.Data.MIMETYPE))) {
                    "vnd.android.cursor.item/vnd.com.whatsapp.voip.call" -> contactsVOIP.add("Display name: $displayName ID: $id")
                    "vnd.android.cursor.item/vnd.com.whatsapp.video.call" -> contactsVideo.add("Display name: $displayName ID: $id")
                }
            }

            Log.e("CONTACTOS VIDEO", "----------------------------------------")
            contactsVideo.forEach { Log.e("CONTACTOS VIDEO", it) }
            Log.e("CONTACTOS VIDEO", "----------------------------------------")

            Log.e("CONTACTOS VoIP", "----------------------------------------")
            contactsVOIP.forEach { Log.e("CONTACTOS VoIP", it) }
            Log.e("CONTACTOS VoIP", "----------------------------------------")
            cursor.close()
        }
    }

}