package com.milord.coursework.main

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.milord.coursework.data.UserData
import com.milord.coursework.databinding.FragmentHistoryBinding
import com.milord.coursework.utils.payments.PaymentAdapter
import com.milord.coursework.data.UserViewModel
import com.milord.coursework.utils.api.Payment

class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private val userViewModel = UserViewModel.getInstance()
    private var user : UserData = UserData()
    private lateinit var paymentList : ArrayList<Payment>
    private lateinit var paymentAdapter : PaymentAdapter
    private lateinit var historyList : androidx.recyclerview.widget.RecyclerView

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
        try
        {
            user = userViewModel.userData.value!!
        }
        catch (e: NullPointerException)
        {
            user = UserData()
        }
        historyList = binding.historyListView
        val filterButton = binding.filterButton
        try
        {
            paymentList = user.getPayments()
            paymentAdapter = PaymentAdapter(paymentList)
        }
        catch (e: Exception)
        {
            paymentList = ArrayList()
            paymentAdapter = PaymentAdapter(paymentList)
        }

        userViewModel.userData.observe(viewLifecycleOwner) { userData ->
            user = userData
            update(user.getPayments())
        }

        filterButton.setOnClickListener {
            val dialog = DatePickerDialog(requireContext())
            dialog.show()
            dialog.setOnDateSetListener { _, year, month, dayOfMonth ->
                val date = "$year-${month+1}-$dayOfMonth"
                val filteredList =
                    user.getPayments().filter { it.date.startsWith(date) } as ArrayList<Payment>
                update(filteredList)
            }
        }
        historyList.layoutManager = LinearLayoutManager(context)
        historyList.setHasFixedSize(false)
        historyList.adapter = paymentAdapter
    }

    private fun update(newList:ArrayList<Payment>){
        paymentList = newList
        paymentList.reverse()
        paymentAdapter = PaymentAdapter(paymentList)
        historyList.adapter = paymentAdapter
    }
}