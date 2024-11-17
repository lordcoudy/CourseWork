package com.milord.coursework.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.milord.coursework.MainActivity
import com.milord.coursework.data.BalanceData
import com.milord.coursework.data.InfoLoader
import com.milord.coursework.data.PaymentsDates
import com.milord.coursework.data.UserData
import com.milord.coursework.databinding.ActivityAuthBinding
import com.milord.coursework.utils.SaveSharedPreference
import com.milord.coursework.utils.UserViewModel

class AuthActivity : AppCompatActivity()
{
    private var conditions = false
    private var data = false
    private var notification = false
    private var emailEntered = false
    private var passwordEntered = false
    private var user : UserData? = null

    private lateinit var binding: ActivityAuthBinding
    private val userViewModel = UserViewModel.getInstance()
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val editTextEmail = binding.editTextTextEmailAddress
        val editTextPassword = binding.editTextTextPassword
        val buttonLogin = binding.buttonLogin
        val buttonRegister = binding.buttonRegister
        val checkBoxConditions = binding.checkBoxConditions
        val checkBoxData = binding.checkBoxData
        val checkBoxNotification = binding.checkBoxNotification

        checkBoxConditions.setOnClickListener {
            conditions = checkBoxConditions.isChecked
            emailEntered = editTextEmail.text.isNotEmpty()
            passwordEntered = editTextPassword.text.isNotEmpty()
            buttonLogin.isEnabled = conditions && data && notification && emailEntered && passwordEntered
            buttonRegister.isEnabled = conditions && data && notification && emailEntered && passwordEntered
        }

        checkBoxData.setOnClickListener {
            data = checkBoxData.isChecked
            emailEntered = editTextEmail.text.isNotEmpty()
            passwordEntered = editTextPassword.text.isNotEmpty()
            buttonLogin.isEnabled = conditions && data && notification && emailEntered && passwordEntered
            buttonRegister.isEnabled = conditions && data && notification && emailEntered && passwordEntered
        }

        checkBoxNotification.setOnClickListener {
            notification = checkBoxNotification.isChecked
            emailEntered = editTextEmail.text.isNotEmpty()
            passwordEntered = editTextPassword.text.isNotEmpty()
            buttonLogin.isEnabled = conditions && data && notification && emailEntered && passwordEntered
            buttonRegister.isEnabled = conditions && data && notification && emailEntered && passwordEntered
        }

        buttonLogin.setOnClickListener {
            // TODO: check if the user exists in the database
            // TODO: load user data from the database
            val infoLoader = InfoLoader()
            infoLoader.loadData(editTextEmail.text.toString(), editTextPassword.text.toString())
            user = infoLoader.getData()
            userViewModel.updateUser(user!!)
            logInUser()
        }

        buttonRegister.setOnClickListener {
            if (editTextPassword.length() < 6)
            {
                Toast.makeText(this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            user = UserData(editTextEmail.text.toString(), editTextPassword.text.toString())
            userViewModel.updateUser(user!!)
            SaveSharedPreference().setUserName(this, user!!.getEmail())
            SaveSharedPreference().setPassword(this, user!!.getPassword())
            startActivity(Intent(this, RegistrationActivity::class.java))
            finish()
        }

    }

    private fun logInUser()
    {
        val pref = getSharedPreferences("logIn", Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putBoolean("isLoggedIn", true)
        editor.apply()
        SaveSharedPreference().setUserName(this, user!!.getEmail())
        SaveSharedPreference().setPassword(this, user!!.getPassword())
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}