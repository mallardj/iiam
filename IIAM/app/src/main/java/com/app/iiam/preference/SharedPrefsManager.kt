package com.app.iiam.preference

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

object SharedPrefsManager {

    private val PREF_NAME = "IIAMSharedFile"
    private lateinit var prefs: SharedPreferences

    fun initialize(appContext: Context?) {
        prefs = appContext!!.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    @SuppressLint("ApplySharedPref")
    fun clearPrefs() {
        val editor = prefs.edit()
        editor.clear()
        editor.commit()
    }

    @SuppressLint("ApplySharedPref")
    fun removeKey(key: String) {
        val editor = prefs.edit()
        editor.remove(key)
        editor.commit()
    }

    @SuppressLint("ApplySharedPref")
    fun containsKey(key: String): Boolean {
        return prefs.contains(key)
    }

    @SuppressLint("ApplySharedPref")
    fun setString(key: String, value: String?) {
        val editor = prefs.edit()
        editor.putString(key, value)
        editor.commit()
    }

    @SuppressLint("ApplySharedPref")
    fun getString(key: String): String {
        return prefs.getString(key, "") ?: ""
    }

    @SuppressLint("ApplySharedPref")
    fun setInt(key: String, value: Int?) {
        val editor = prefs.edit()
        editor.putInt(key, value!!)
        editor.commit()
    }

    @SuppressLint("ApplySharedPref")
    fun getInt(key: String): Int {
        return prefs.getInt(key, 0)
    }

    @SuppressLint("ApplySharedPref")
    fun setBoolean(key: String, value: Boolean) {
        val editor = prefs.edit()
        editor.putBoolean(key, value)
        editor.commit()
    }

    @SuppressLint("ApplySharedPref")
    fun getBoolean(key: String): Boolean {
        return prefs.getBoolean(key, false)
    }

    @SuppressLint("ApplySharedPref")
    fun getBooleanPositive(key: String): Boolean {
        return prefs.getBoolean(key, true)
    }

    @SuppressLint("ApplySharedPref")
    fun setFloat(key: String, value: Float) {
        val editor = prefs.edit()
        editor.putFloat(key, value)
        editor.commit()
    }

    @SuppressLint("ApplySharedPref")
    fun getFloat(key: String): Float {
        return prefs.getFloat(key, 0f)
    }

    @SuppressLint("ApplySharedPref")
    fun setLong(key: String, value: Long) {
        val editor = prefs.edit()
        editor.putLong(key, value)
        editor.commit()
    }

    @SuppressLint("ApplySharedPref")
    fun getLong(key: String): Long {
        return prefs.getLong(key, 0)
    }
}