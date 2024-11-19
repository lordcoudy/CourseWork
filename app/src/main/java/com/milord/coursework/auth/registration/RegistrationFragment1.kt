package com.milord.coursework.auth.registration

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.milord.coursework.R
import com.milord.coursework.auth.AuthActivity
import com.milord.coursework.data.UserData
import com.milord.coursework.databinding.FragmentRegistration1Binding
import com.milord.coursework.utils.UserViewModel


class RegistrationFragment1 : Fragment()
{
    private lateinit var binding: FragmentRegistration1Binding
    private val userViewModel = UserViewModel.getInstance()
    private var user: UserData? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View
    {
        binding = FragmentRegistration1Binding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        val buttonNext = binding.nextButton
        val buttonBack = binding.backButton
        val surname = binding.editTextTextPersonSurname
        val name = binding.editTextTextPersonName
        val middleName = binding.editTextTextPersonMiddlename
        user = userViewModel.userData.value

        userViewModel.userData.observe(viewLifecycleOwner) { userData ->
            user = userData
        }

        buttonNext.setOnClickListener {
            if (name.text.toString().isNotEmpty() && surname.text.toString().isNotEmpty())
            {
                val fullName = "${surname.text} ${name.text} ${middleName.text}"
                user?.setName(fullName)
            }
            user?.let { it1 -> userViewModel.updateUser(it1) }
            findNavController().navigate(R.id.action_registrationFragment1_to_registrationFragment2)
        }
        buttonBack.setOnClickListener {
            requireActivity().startActivityFromFragment(
                this,
                Intent(requireActivity(), AuthActivity::class.java),
                0
            )
            requireActivity().finish()
        }
    }
}