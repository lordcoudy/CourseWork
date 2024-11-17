package com.milord.coursework

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.milord.coursework.data.UserData
import com.milord.coursework.databinding.FragmentHistoryBinding
import com.milord.coursework.utils.UserViewModel

class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private val userViewModel = UserViewModel.getInstance()
    private var user : UserData? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        user = userViewModel.userData.value
        val historyList = binding.historyListView

        userViewModel.userData.observe(viewLifecycleOwner) { userData ->
            user = userData
        }
        // TODO: create history list
    }
}