package com.milord.coursework.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager


class SaveSharedPreference
{
    val PREF_USER_NAME: String = "username"
    val PREF_PASSWORD: String = "password"

    fun getSharedPreferences(ctx: Context?): SharedPreferences
    {
        return PreferenceManager.getDefaultSharedPreferences(ctx)
    }

    fun setUserName(ctx: Context?, userName: String?)
    {
        val editor = getSharedPreferences(ctx).edit()
        editor.putString(PREF_USER_NAME, userName)
        editor.commit()
    }

    fun setPassword(ctx: Context?, password: String?)
    {
        val editor = getSharedPreferences(ctx).edit()
        editor.putString(PREF_PASSWORD, password)
        editor.commit()
    }

    fun getUserName(ctx: Context?): String?
    {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "")
    }

    fun getPassword(ctx: Context?): String?
    {
        return getSharedPreferences(ctx).getString(PREF_PASSWORD, "")
    }
}