package com.milord.coursework.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.milord.coursework.R
import com.milord.coursework.utils.api.LoginRequest
import com.milord.coursework.utils.api.AuthResponse
import com.milord.coursework.main.MainActivity
import com.milord.coursework.utils.api.ApiClient
import com.milord.coursework.data.UserData
import com.milord.coursework.databinding.ActivityAuthBinding
import com.milord.coursework.data.prefs.SaveSharedPreference
import com.milord.coursework.data.UserViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthActivity : AppCompatActivity()
{
    private var conditions = false
    private var data = false
    private var emailEntered = false
    private var passwordEntered = false
    private var user : UserData = UserData()

    private lateinit var binding: ActivityAuthBinding
    private val userViewModel = UserViewModel.getInstance()
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
            if (!isValidEmail(editTextEmail.text.toString()))
            {
                editTextEmail.error = getString(R.string.invalid_email)
                return@setOnClickListener
            }
            if (!isValidPassword(editTextPassword.text.toString()))
            {
                editTextPassword.error = getString(R.string.password_must_be_at_least_6_characters_long)
                return@setOnClickListener
            }
            val loginRequest = LoginRequest(email = editTextEmail.text.toString(), password = editTextPassword.text.toString())
            apiClient.getApiService().login(loginRequest)
                .enqueue(object : Callback<AuthResponse>
                {
                    override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                        Toast.makeText(this@AuthActivity, getString(R.string.something_went_wrong, t.message), Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                        val loginResponse = response.body()

                        if (response.code() == 200) {
                            SaveSharedPreference(this@AuthActivity).setToken(loginResponse!!.authToken)
                            user = UserData(editTextEmail.text.toString())
                            user.setToken(loginResponse!!.authToken)
                            updateUser()
                            logInUser()
                        } else {
                            Toast.makeText(this@AuthActivity,
                                getString(R.string.email_or_pwd_wrong, response.message()), Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            return@setOnClickListener
        }

        buttonRegister.setOnClickListener {
            if (!isValidEmail(editTextEmail.text.toString()))
            {
                Toast.makeText(this, getString(R.string.invalid_email), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!isValidPassword(editTextPassword.text.toString()))
            {
                Toast.makeText(this,
                    getString(R.string.password_must_be_at_least_6_characters_long), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            user = UserData(editTextEmail.text.toString())
            SaveSharedPreference(this).setPassword(editTextPassword.text.toString())
            updateUser()
            userViewModel.updateUser(user)
            startActivity(Intent(this, RegistrationActivity::class.java))
            finish()
        }
    }

    private fun updateUser()
    {
        SaveSharedPreference(this).setToken(user!!.getToken())
    }

    private fun logInUser()
    {
        SaveSharedPreference(this).setLogIn(true)
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