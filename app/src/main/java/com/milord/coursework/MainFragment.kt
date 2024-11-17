package com.milord.coursework

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.milord.coursework.data.UserData
import com.milord.coursework.databinding.FragmentMainBinding
import com.milord.coursework.utils.NotificationDialogFragment
import com.milord.coursework.utils.UserViewModel

enum class Data
{
    Electricity,
    ColdWater,
    HotWater,
    Balance
}

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private val userViewModel = UserViewModel.getInstance()
    private var user : UserData? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {

        super.onViewCreated(view, savedInstanceState)
        user = userViewModel.userData.value

        val balanceButton = binding.balanceButton
        val electricityText = binding.electricityText
        val coldWaterText = binding.coldWaterText
        val hotWaterText = binding.hotWaterText
        val electricityDate = binding.electricityDate
        val coldWaterDate = binding.coldWaterDate
        val hotWaterDate = binding.hotWaterDate
        val electricityButton = binding.refreshElectricityButton
        val coldWaterButton = binding.refreshColdWaterButton
        val hotWaterButton = binding.refreshHotWaterButton
        val balanceUpdate = binding.refreshBalanceButton
        val notification = binding.BalanceIcon
        val INN = binding.INNTw

        userViewModel.userData.observe(viewLifecycleOwner) { userData ->
            user = userData
            INN.text = user?.getINN()
            balanceButton.text = user!!.getBalance().balance.toString()
            electricityText.text = user!!.getBalance().electricity.toString()
            coldWaterText.text = user!!.getBalance().coldWater.toString()
            hotWaterText.text = user!!.getBalance().hotWater.toString()
            electricityDate.text = user!!.getDates().electricityDate.toString()
            coldWaterDate.text = user!!.getDates().coldWaterDate.toString()
            hotWaterDate.text = user!!.getDates().hotWaterDate.toString()
            if (user!!.getBalance().balance < userData.getBalance().cap)
            {
                notification.visibility = View.VISIBLE
            }
        }

        electricityButton.setOnClickListener()
        {
            updateData(Data.Electricity)
        }
        coldWaterButton.setOnClickListener()
        {
            updateData(Data.ColdWater)
        }
        hotWaterButton.setOnClickListener()
        {
            updateData(Data.HotWater)
        }
        balanceUpdate.setOnClickListener()
        {
            updateData(Data.Balance)
        }

        balanceButton.setOnClickListener()
        {
            findNavController().navigate(R.id.action_navigation_home_to_paymentFragment)
        }

        notification.setOnClickListener()
        {
            val myDialogFragment = NotificationDialogFragment()
            val manager = requireActivity().supportFragmentManager
            //myDialogFragment.show(manager, "dialog")
            val transaction: FragmentTransaction = manager.beginTransaction()
            myDialogFragment.show(transaction, "dialog")
        }

    }

    private fun updateData(data : Data)
    {
        // TODO: Load data from db
        when (data)
        {
            Data.Electricity ->
            {
                user!!.getBalance().electricity = binding.electricityText.text.toString().toDouble()
                userViewModel.updateUser(user!!)
            }

            Data.ColdWater ->
            {
                user!!.getBalance().coldWater = binding.coldWaterText.text.toString().toDouble()
                userViewModel.updateUser(user!!)
            }

            Data.HotWater ->
            {
                user!!.getBalance().hotWater = binding.hotWaterText.text.toString().toDouble()
                userViewModel.updateUser(user!!)
            }

            Data.Balance ->
            {
                user!!.getBalance().balance = binding.balanceButton.text.toString().toDouble()
                userViewModel.updateUser(user!!)
            }
        }
    }
}