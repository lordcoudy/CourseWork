package com.milord.coursework.auth.registration

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.milord.coursework.main.MainActivity
import com.milord.coursework.R
import com.milord.coursework.auth.AuthActivity
import com.milord.coursework.utils.api.AuthResponse
import com.milord.coursework.utils.api.RegisterRequest
import com.milord.coursework.utils.api.ApiClient
import com.milord.coursework.data.UserData
import com.milord.coursework.databinding.FragmentRegistration3Binding
import com.milord.coursework.data.prefs.SaveSharedPreference
import com.milord.coursework.data.UserViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegistrationFragment3 : Fragment()
{
    private lateinit var binding: FragmentRegistration3Binding
    private val userViewModel = UserViewModel.getInstance()
    private var user : UserData? = null
    private lateinit var apiClient: ApiClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View
    {
        binding = FragmentRegistration3Binding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        user = userViewModel.userData.value
        val buttonNext = binding.nextButton3
        val buttonBack = binding.backButton3
        val INNEt = binding.editTextINN

        userViewModel.userData.observe(viewLifecycleOwner) { userData ->
            user = userData
        }

        buttonNext.setOnClickListener {
            if (INNEt.text.toString().isNotEmpty())
            {
                user?.setINN(INNEt.text.toString())
                user?.let { it1 -> userViewModel.updateUser(it1) }
            }
            else
            {
                INNEt.error = getString(R.string.inn_is_required)
                return@setOnClickListener
            }
            apiClient = ApiClient()
            apiClient.getApiService().register(RegisterRequest(
                email = user!!.getEmail(), password = SaveSharedPreference(requireContext()).getPassword()!!,
                passwordConfirmation = SaveSharedPreference(requireContext()).getPassword()!!,
                name = user!!.getName(), INN = user!!.getINN(), address = user!!.getAddress())
            ).enqueue(object : Callback<AuthResponse>
                {
                    override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                        Toast.makeText(context,
                            getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
                        SaveSharedPreference(requireContext()).clearPassword()
                    }

                    override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                        val registerResponse = response.body()

                        if (registerResponse?.tokenType == "Bearer" && registerResponse.authToken.isNotEmpty()) {
                            SaveSharedPreference(requireContext()).setToken(registerResponse.authToken)
                            user?.setToken(registerResponse.authToken)
                            userViewModel.updateUser(user!!)
                            SaveSharedPreference(requireContext()).setLogIn(true)
                            SaveSharedPreference(requireContext()).clearPassword()
                            requireActivity().startActivityFromFragment(this@RegistrationFragment3, Intent(requireActivity(), MainActivity::class.java), 0)
                            requireActivity().finish()
                        } else {
                            Toast.makeText(context,
                                getString(R.string.registration_error, registerResponse?.message), Toast.LENGTH_SHORT).show()
                            SaveSharedPreference(requireContext()).clearPassword()
                            requireActivity().startActivityFromFragment(
                                this@RegistrationFragment3,
                                Intent(requireActivity(), AuthActivity::class.java),
                                0
                            )
                            requireActivity().finish()
                        }
                    }
                })
            return@setOnClickListener
        }

        buttonBack.setOnClickListener {
            if (INNEt.text.toString().isNotEmpty())
            {
                user?.setINN(INNEt.text.toString())
                user?.let { it1 -> userViewModel.updateUser(it1) }
            }
            findNavController().navigate(R.id.action_registrationFragment2_to_registrationFragment3)
        }
    }
}