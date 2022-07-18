package com.cinescape1.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import android.view.View
import com.cinescape1.R

class MyReceiver : BroadcastReceiver() {
    var dialog: Dialog? = null
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {
        var status = NetworkUtil.getConnectivityStatusString(context)
        dialog = Dialog(context, android.R.style.ThemeOverlay_Material_ActionBar)
        dialog!!.setContentView(R.layout.internet_dialog)
        val restart = dialog!!.findViewById<View>(R.id.view49)
        val setting = dialog!!.findViewById<View>(R.id.view48)
        restart.setOnClickListener { v: View? ->
            (context as Activity).finish()
            context.startActivity(context.intent)
            context.overridePendingTransition(0, 0)
        }
        setting.setOnClickListener {
            (context as Activity).startActivity(
                Intent(
                    Settings.ACTION_WIRELESS_SETTINGS
                )
            )
        }
        Log.d("network", status)
        if (status.isEmpty() || status == "No internet is available" || status == "No Internet Connection") {
            status = "No Internet Connection"
            dialog?.show()
        }
        else{
            dialog?.hide()
        }
//        Toast.makeText(context, status, Toast.LENGTH_LONG).show()
    }
}