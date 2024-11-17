package com.milord.coursework.utils

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.milord.coursework.data.UserData

class UserViewModel : ViewModel() {
    private val _userData = MutableLiveData<UserData>()
    val userData: LiveData<UserData> = _userData

    fun updateUser(user: UserData) {
        _userData.value = user
    }

    companion object{
        private lateinit var instance: UserViewModel

        @MainThread
        fun getInstance(): UserViewModel{
            instance = if(::instance.isInitialized) instance else UserViewModel()
            return instance
        }
    }
}