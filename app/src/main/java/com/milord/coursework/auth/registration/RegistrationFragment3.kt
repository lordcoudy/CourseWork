package com.milord.coursework.auth.registration

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.milord.coursework.MainActivity
import com.milord.coursework.R
import com.milord.coursework.auth.AuthActivity
import com.milord.coursework.data.InfoLoader
import com.milord.coursework.data.UserData
import com.milord.coursework.databinding.FragmentRegistration3Binding
import com.milord.coursework.utils.UserViewModel


class RegistrationFragment3 : Fragment()
{
    private lateinit var binding: FragmentRegistration3Binding
    private val userViewModel = UserViewModel.getInstance()
    private var user : UserData? = null

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
                INNEt.error = "INN is required"
                return@setOnClickListener
            }
            val pref = requireActivity().getSharedPreferences("logIn", Context.MODE_PRIVATE)
            val editor = pref.edit()
            editor.putBoolean("isLoggedIn", true)
            editor.apply()
            requireActivity().startActivityFromFragment(this, Intent(requireActivity(), MainActivity::class.java), 0)
            requireActivity().finish()
        }

        buttonBack.setOnClickListener {
            if (INNEt.text.toString().isNotEmpty())
            {
                user?.setINN(INNEt.text.toString())
                val infoLoader = InfoLoader()
                infoLoader.loadData(user!!.getEmail(), user!!.getPassword())
                user = infoLoader.getData()
                user?.let { it1 -> userViewModel.updateUser(it1) }
            }
            findNavController().navigate(R.id.action_registrationFragment2_to_registrationFragment3)
        }
    }
}