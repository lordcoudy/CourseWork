package com.milord.coursework.utils
import android.content.Context;
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen


import com.milord.coursework.MainActivity;
import com.milord.coursework.auth.AuthActivity
import com.milord.coursework.data.UserData

class Routing : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

// Keep the splash screen visible for this Activity.
        splashScreen.setKeepOnScreenCondition { true }
        Handler(Looper.getMainLooper()).postDelayed(2000) {
            ////////////////////////////////////////////////////////////////////////////////////////////////
            /*
            shared preference code for keeping users logged IN.
            shared preferences are local data storage system to store data with key and value.
             */
            val pref = getSharedPreferences("logIn", Context.MODE_PRIVATE)
            val isLoggedIn = pref.getBoolean("isLoggedIn", false)
            ////////////////////////////////////////////////////////////////////////////////////////////////
            if (isLoggedIn) {
                val user : UserData? = SaveSharedPreference().getUserName(this)
                    ?.let { SaveSharedPreference().getPassword(this)
                        ?.let { it1 -> UserData(it, it1) } }
                user?.let { UserViewModel.getInstance().updateUser(it) }
            }
            //Decide whether to start LogInPage or HomePage as per the user logIn status
            val targetActivity = if (isLoggedIn) MainActivity::class.java else AuthActivity::class.java
            startActivity(Intent(this, targetActivity))
            finish()
        }

    }
}