package com.milord.coursework.data.prefs

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.core.content.ContextCompat.getString
import com.milord.coursework.R


class SaveSharedPreference (context: Context)
{
    companion object {
        const val PREF_USER_TOKEN = "access_token"
        const val PREF_USER_NAME: String = "username"
        const val PREF_PASSWORD: String = "password"
        const val PREF_LOGIN: String = "logIn"
    }
    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    fun setEmail(userName: String?)
    {
        val editor = prefs.edit()
        editor.putString(PREF_USER_NAME, userName)
        editor.apply()
    }

    fun setPassword(password: String?)
    {
        val editor = prefs.edit()
        editor.putString(PREF_PASSWORD, password)
        editor.apply()
    }

    fun setToken(token: String?)
    {
        val editor = prefs.edit()
        editor.putString(PREF_USER_TOKEN, token)
        editor.apply()
    }

    fun setLogIn(logIn: Boolean)
    {
        val editor = prefs.edit()
        editor.putBoolean(PREF_LOGIN, logIn)
        editor.apply()
    }

    fun getUserName(): String?
    {
        return prefs.getString(PREF_USER_NAME, "")
    }

    fun getPassword(): String?
    {
        return prefs.getString(PREF_PASSWORD, "")
    }

    fun getToken(): String?
    {
        return prefs.getString(PREF_USER_TOKEN, "")
    }

    fun getLogIn(): Boolean
    {
        return prefs.getBoolean(PREF_LOGIN, false)
    }
}