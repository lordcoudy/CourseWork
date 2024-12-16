package com.milord.coursework.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.milord.coursework.data.UserData
import com.milord.coursework.databinding.FragmentSettingsBinding
import com.milord.coursework.utils.Routing
import com.milord.coursework.data.UserViewModel
import com.milord.coursework.data.prefs.SaveSharedPreference

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val userViewModel = UserViewModel.getInstance()
    private var user : UserData = UserData()

    companion object {
        const val TELEGRAM = "https://t.me/MyMilord"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        val changeAcc = binding.addAccountButton
        val logOutButton = binding.exitButton
        val accountTw = binding.accountText
        val supportButton = binding.supportButton

        user = userViewModel.userData.value!!
        userViewModel.userData.observe(viewLifecycleOwner) { userData ->
            user = userData
            accountTw.text = buildString {
                append(user.getName())
                append("\n")
                append(user.getEmail())
            }
        }

        changeAcc.setOnClickListener {
            SaveSharedPreference(requireContext()).setLogIn(false)
            val intent = Intent(requireContext(), Routing::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            requireActivity().finish()
        }

        logOutButton.setOnClickListener {
            requireActivity().finish()
            System.out.close()
        }

        supportButton.setOnClickListener {
            val uri: Uri = Uri.parse(TELEGRAM)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
    }
}