package com.cinescape1.data.preference

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPreferences @Inject constructor(){
    var preferenceName = "PiInsite"
    var preference: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null
    val TAG = "ConsultantPreferences======="

    fun getInstance(): AppPreferences? {
        return this
    }

    //create context in app module for using dagger in shared Preferecees
    @SuppressLint("CommitPrefEdits")
    @Inject
    fun ConsultantPreferences(ctx: Context) {
        try {
            preference =
                ctx.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
            editor = preference!!.edit()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun clearData() {
        if (editor != null) {
            editor!!.clear()
            editor!!.commit()
        }
    }

    fun clearString(key: String?) {
        if (editor != null) {
            editor!!.remove(key)
            editor!!.commit()
        }
    }

    fun putString(key: String?, value: String?) {
        if (editor != null) {
            editor!!.putString(key, value)
            editor!!.commit()
        }
    }

    /*public void putBitmap(String key,Bitmap value )
    {
        editor.putString(key, PCBaseActivity.encodeTobase64(value));
        editor.commit();
    }*/

    /*public void putBitmap(String key,Bitmap value )
    {
        editor.putString(key, PCBaseActivity.encodeTobase64(value));
        editor.commit();
    }*/
    fun getBitmap(key: String?): String? {
        return if (preference != null) preference!!.getString(key, "") else ""
    }

    fun putInt(key: String?, value: Int) {
        if (editor != null) {
            editor!!.putInt(key, value)
            editor!!.commit()
        }
    }

    fun putBoolean(key: String?, value: Boolean) {
        if (editor != null) {
            editor!!.putBoolean(key, value)
            editor!!.commit()
        }
    }


    fun getBoolean(key: String?): Boolean {
        return if (preference != null) preference!!.getBoolean(key, false) else false
    }

    fun getString(key: String?): String? {
        return if (preference != null) preference!!.getString(key, "") else ""
    }

    fun getInt(key: String?): Int {
        return if (preference != null) preference!!.getInt(key, 0) else 0
    }


    fun putLong(key: String?, value: Long?) {
        if (editor != null) {
            editor!!.putLong(key, value!!)
            editor!!.commit()
        }
    }

    fun getLong(key: String?): Long {
        return if (preference != null) preference!!.getLong(key, 0) else 0
    }


}

