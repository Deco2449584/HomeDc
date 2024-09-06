package com.domocodetech.homedc.utils

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesManager {
    private const val PREF_NAME = "HomeDcPrefs"
    private const val KEY_AUTH_TOKEN = "auth_token"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveAuthToken(context: Context, token: String) {
        val editor = getPreferences(context).edit()
        editor.putString(KEY_AUTH_TOKEN, token)
        editor.apply()
    }

    fun getAuthToken(context: Context): String? {
        return getPreferences(context).getString(KEY_AUTH_TOKEN, null)
    }

    fun clearAuthToken(context: Context) {
        val editor = getPreferences(context).edit()
        editor.remove(KEY_AUTH_TOKEN)
        editor.apply()
    }
}