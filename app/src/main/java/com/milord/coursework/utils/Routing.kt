package com.milord.coursework.utils
import android.content.Context;
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen


import com.milord.coursework.main.MainActivity;
import com.milord.coursework.auth.AuthActivity
import com.milord.coursework.data.UserData
import com.milord.coursework.data.UserViewModel
import com.milord.coursework.data.prefs.SaveSharedPreference

class Routing : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition { true }
        Handler(Looper.getMainLooper()).postDelayed(2000) {
            val isLoggedIn = SaveSharedPreference(this).getLogIn()
            val targetActivity = if (isLoggedIn) MainActivity::class.java else AuthActivity::class.java
            startActivity(Intent(this, targetActivity))
            finish()
        }
    }
}