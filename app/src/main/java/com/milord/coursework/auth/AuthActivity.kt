package com.milord.coursework.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.milord.coursework.MainActivity
import com.milord.coursework.data.ApiClient
import com.milord.coursework.data.InfoLoader
import com.milord.coursework.data.SessionManager
import com.milord.coursework.data.UserData
import com.milord.coursework.databinding.ActivityAuthBinding
import com.milord.coursework.utils.SaveSharedPreference
import com.milord.coursework.utils.UserViewModel

class AuthActivity : AppCompatActivity()
{
    private var conditions = false
    private var data = false
    private var emailEntered = false
    private var passwordEntered = false
    private var user : UserData? = null

    private lateinit var binding: ActivityAuthBinding
    private val userViewModel = UserViewModel.getInstance()
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
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

        checkBoxConditions.setOnClickListener {
            conditions = checkBoxConditions.isChecked
            emailEntered = editTextEmail.text.isNotEmpty()
            passwordEntered = editTextPassword.text.isNotEmpty()
            buttonLogin.isEnabled = conditions && data  && emailEntered && passwordEntered
            buttonRegister.isEnabled = conditions && data && emailEntered && passwordEntered
        }

        checkBoxData.setOnClickListener {
            data = checkBoxData.isChecked
            emailEntered = editTextEmail.text.isNotEmpty()
            passwordEntered = editTextPassword.text.isNotEmpty()
            buttonLogin.isEnabled = conditions && data && emailEntered && passwordEntered
            buttonRegister.isEnabled = conditions && data && emailEntered && passwordEntered
        }

        buttonLogin.setOnClickListener {
            apiClient = ApiClient()
            sessionManager = SessionManager(this)
            var logged = false
            val infoLoader = InfoLoader()   // Временная заглушка
            if (!isValidEmail(editTextEmail.text.toString()))
            {
                Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!isValidPassword(editTextPassword.text.toString()))
            {
                Toast.makeText(this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            infoLoader.loadData(editTextEmail.text.toString(), editTextPassword.text.toString())
            user = infoLoader.getData()
            updateUser()
            logInUser()
        }

        buttonRegister.setOnClickListener {
            if (!isValidEmail(editTextEmail.text.toString()))
            {
                Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!isValidPassword(editTextPassword.text.toString()))
            {
                Toast.makeText(this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            user = UserData(editTextEmail.text.toString(), editTextPassword.text.toString())
            updateUser()
            startActivity(Intent(this, RegistrationActivity::class.java))
            finish()
        }

    }

    private fun updateUser()
    {
        userViewModel.updateUser(user!!)
        SaveSharedPreference().setEmail(this, user!!.getEmail())
        SaveSharedPreference().setPassword(this, user!!.getPassword())
        SaveSharedPreference().setToken(this, user!!.getToken())
    }

    private fun logInUser()
    {
        val pref = getSharedPreferences("logIn", Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putBoolean("isLoggedIn", true)
        editor.apply()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun isValidEmail(email: String): Boolean
    {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean
    {
        return password.length >= 6
    }
}