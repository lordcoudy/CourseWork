package com.milord.coursework.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.milord.coursework.R
import com.milord.coursework.data.Payment
import com.milord.coursework.data.UserData
import com.milord.coursework.data.currentDate
import com.milord.coursework.databinding.FragmentPaymentBinding
import com.milord.coursework.data.UserViewModel

class PaymentFragment : Fragment()
{
    private lateinit var binding : FragmentPaymentBinding
    private val userViewModel = UserViewModel.getInstance()
    private var user : UserData? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View
    {
        binding = FragmentPaymentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        val buttonPay = binding.payButton
        val buttonBack = binding.payBackButton
        val addedBalanceEt = binding.paySum
        val INNTw = binding.INNTw
        val currentBalance = binding.balanceTw

        user = userViewModel.userData.value

        userViewModel.userData.observe(viewLifecycleOwner) { userData ->
            user = userData
            INNTw.text = user?.getINN()
            currentBalance.text = user?.getBalance()?.balance.toString()
        }

        buttonPay.setOnClickListener {
            val balanceData = user?.getBalance()
            balanceData!!.balance += addedBalanceEt.text.toString().toDouble()
            balanceData?.let { it1 -> user?.setBalance(it1) }
            user!!.addPayment(Payment(currentDate, user!!.getBalance().balance.toString()))
            userViewModel.updateUser(user!!)
            findNavController().navigate(R.id.action_paymentFragment_to_navigation_home)
        }

        buttonBack.setOnClickListener {
            findNavController().navigate(R.id.action_paymentFragment_to_navigation_home)
        }
    }
}