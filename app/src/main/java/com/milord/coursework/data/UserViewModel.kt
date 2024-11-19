package com.milord.coursework.data

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserViewModel : ViewModel() {
    private val _userData = MutableLiveData<UserData>()
    val userData: LiveData<UserData> = _userData

    fun updateUser(user: UserData) {
        _userData.value = user
    }

    companion object{
        private lateinit var instance: UserViewModel

        @MainThread
        fun getInstance(): UserViewModel
        {
            instance = if(Companion::instance.isInitialized) instance else UserViewModel()
            return instance
        }
    }
}