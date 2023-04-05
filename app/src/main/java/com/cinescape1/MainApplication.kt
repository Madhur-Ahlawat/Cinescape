package com.cinescape1


import android.content.BroadcastReceiver
import android.content.Context
import android.content.SharedPreferences
import com.cinescape1.di.DaggerAppComponent
import com.cinescape1.utils.Constant
import com.cinescape1.utils.LocaleHelper
import com.instabug.library.Instabug
import com.instabug.library.invocation.InstabugInvocationEvent
import com.onesignal.OneSignal
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication


class MainApplication : DaggerApplication() {
    private val ONESIGNAL_APP_ID = "b62fc843-477b-4cba-802f-9bc0bded6c5a"

    private var broadcastReceiver: BroadcastReceiver? = null

    override fun applicationInjector(): AndroidInjector<out MainApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }

    override fun onCreate() {
//        FacebookSdk.sdkInitialize(applicationContext)
        super.onCreate()
        Instabug.Builder(this, "3eec3a131dd9fbce57094276934f6eab")
            .setInvocationEvents(
                InstabugInvocationEvent.SHAKE,
                InstabugInvocationEvent.FLOATING_BUTTON
            )
            .build()
        val preferences: SharedPreferences = getSharedPreferences("PiInsite", Context.MODE_PRIVATE)
        when {
            preferences.getString(Constant.IntentKey.SELECT_LANGUAGE, "") == "ar" -> {
                LocaleHelper.setLocale(this, "ar")
                println(
                    "getLocalLanguage--->${
                        preferences.getString(
                            Constant.IntentKey.SELECT_LANGUAGE,
                            ""
                        )
                    }"
                )
            }
            preferences.getString(Constant.IntentKey.SELECT_LANGUAGE, "") == "en" -> {
                LocaleHelper.setLocale(this, "en")
            }
            else -> {
                LocaleHelper.setLocale(this, "en")
            }
        }
//        broadcastReceiver = MyReceiver()
//        broadcastIntent()
        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);
    }
//    private fun broadcastIntent() {
//        registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
//    }


//   fun onPause() {
//        super.onPause()
//        unregisterReceiver(MyReceiver)
//    }


}
