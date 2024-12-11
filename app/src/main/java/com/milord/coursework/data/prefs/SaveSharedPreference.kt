package com.milord.coursework.data.prefs

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.core.content.ContextCompat.getString
import com.milord.coursework.R


class SaveSharedPreference (context: Context)
{
    companion object {
        const val PREF_LOGIN = "login"
        const val PREF_USER_TOKEN = "access_token"
        const val PREF_PASSWORD = ""
    }
    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    fun setToken(token: String?)
    {
        val editor = prefs.edit()
        editor.putString(PREF_USER_TOKEN, token)
        editor.apply()
    }

    fun getToken(): String?
    {
        return prefs.getString(PREF_USER_TOKEN, "")
    }

    fun setLogIn(logIn: Boolean)
    {
        val editor = prefs.edit()
        editor.putBoolean(PREF_LOGIN, logIn)
        editor.apply()
    }

    fun getLogIn(): Boolean
    {
        return prefs.getBoolean(PREF_LOGIN, false)
    }

    fun setPassword(password: String)
    {
        val editor = prefs.edit()
        editor.putString(PREF_PASSWORD, password)
        editor.apply()
    }

    fun getPassword(): String?
    {
        return prefs.getString(PREF_PASSWORD, "")
    }

    fun clearPassword()
    {
        val editor = prefs.edit()
        editor.remove(PREF_PASSWORD)
        editor.apply()
    }
}