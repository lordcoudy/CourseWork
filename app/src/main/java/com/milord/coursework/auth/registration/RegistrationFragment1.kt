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
        val radioGroup = binding.radioGroup
        user = userViewModel.userData.value
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId)
            {
                R.id.radioButton1 ->
                {
                    user?.setAddress("Moscow")
                    user?.let { userViewModel.updateUser(it) }
                    binding.radioButton2.isChecked = false
                    binding.radioButton3.isChecked = false
                    binding.moscowView.visibility = View.VISIBLE
                    binding.spbView.visibility = View.GONE
                    binding.kazanView.visibility = View.GONE
                }

                R.id.radioButton2 ->
                {
                    user?.setAddress("Saint-Petersburg")
                    user?.let { userViewModel.updateUser(it) }
                    binding.radioButton1.isChecked = false
                    binding.radioButton3.isChecked = false
                    binding.moscowView.visibility = View.GONE
                    binding.spbView.visibility = View.VISIBLE
                    binding.kazanView.visibility = View.GONE
                }

                R.id.radioButton3 ->
                {
                    user?.setAddress("Kazan")
                    user?.let { userViewModel.updateUser(it) }
                    binding.radioButton1.isChecked = false
                    binding.radioButton2.isChecked = false
                    binding.moscowView.visibility = View.GONE
                    binding.spbView.visibility = View.GONE
                    binding.kazanView.visibility = View.VISIBLE
                }
            }
        }

        userViewModel.userData.observe(viewLifecycleOwner) { userData ->
            user = userData
        }

        buttonNext.setOnClickListener {
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