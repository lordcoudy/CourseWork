package com.milord.coursework.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import com.milord.coursework.R
import com.milord.coursework.data.UserData
import com.milord.coursework.databinding.FragmentMainBinding
import com.milord.coursework.utils.NotificationDialogFragment
import com.milord.coursework.data.UserViewModel

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
            INN.text = buildString {
                append(getString(R.string.payer_code))
                append(": ")
                append(user?.getINN())
            }
            balanceButton.text = buildString {
                append(getString(R.string.balance))
                append(user!!.getBalance().balance.toString())
            }
            electricityText.text = buildString {
                append(getString(R.string.electricity))
                append(user!!.getBalance().electricity.toString())
            }
            coldWaterText.text = buildString {
                append(getString(R.string.cold_water))
                append(user!!.getBalance().coldWater.toString())
            }
            hotWaterText.text = buildString {
                append(getString(R.string.hot_water))
                append(user!!.getBalance().hotWater.toString())
            }
            electricityDate.text = buildString {
                append(getString(R.string.deadline))
                append(user!!.getDates().electricityDate.toString())
            }
            coldWaterDate.text = buildString {
                append(getString(R.string.deadline))
                append(user!!.getDates().coldWaterDate.toString())
            }
            hotWaterDate.text = buildString {
                append(getString(R.string.deadline))
                append(user!!.getDates().hotWaterDate.toString())
            }
            if (user!!.getBalance().balance < userData.getBalance().cap)
            {
                showNotificationDialog()
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
            showNotificationDialog()
        }

    }

    private fun updateData(data : Data)
    {
        // TODO: Load data from db
        when (data)
        {
            Data.Electricity ->
            {
                user!!.getBalance().electricity = binding.electricityText.text.toString().subSequence(15, binding.electricityText.text.length).toString().toDouble()
                userViewModel.updateUser(user!!)
            }

            Data.ColdWater ->
            {
                user!!.getBalance().coldWater = binding.coldWaterText.text.toString().subSequence(5, binding.coldWaterText.text.length).toString().toDouble()
                userViewModel.updateUser(user!!)
            }

            Data.HotWater ->
            {
                user!!.getBalance().hotWater = binding.hotWaterText.text.toString().subSequence(5, binding.hotWaterText.text.length).toString().toDouble()
                userViewModel.updateUser(user!!)
            }

            Data.Balance ->
            {
                user!!.getBalance().balance = binding.balanceButton.text.toString().subSequence(8, binding.balanceButton.text.length).toString().toDouble()
                userViewModel.updateUser(user!!)
            }
        }
    }

    private fun showNotificationDialog()
    {
        val myDialogFragment = NotificationDialogFragment()
        val manager = requireActivity().supportFragmentManager
        //myDialogFragment.show(manager, "dialog")
        val transaction: FragmentTransaction = manager.beginTransaction()
        myDialogFragment.show(transaction, "dialog")
    }
}