package com.mena97villalobos.nanalauncher.ui

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.mena97villalobos.nanalauncher.R
import com.mena97villalobos.nanalauncher.ui.adapter.AppsAdapter
import com.mena97villalobos.nanalauncher.databinding.FragmentMainBinding
import com.mena97villalobos.nanalauncher.model.AppClickListener
import com.mena97villalobos.nanalauncher.model.AppModel


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

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

        setupView();
        return binding.root
    }

    private fun setupView() {
        val adapter = AppsAdapter(requireContext())
        adapter.submitList(getSystemAppList())
        binding.appsList.adapter = adapter

        binding.flashlight.setOnClickListener {

        }
    }

    private fun getSystemAppList() : List<AppModel> {
        val pm: PackageManager = requireContext().packageManager
        val appsList = ArrayList<AppModel>()

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
        return appsList
    }



}