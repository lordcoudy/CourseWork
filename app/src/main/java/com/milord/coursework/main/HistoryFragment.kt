package com.milord.coursework.main

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.milord.coursework.data.Payment
import com.milord.coursework.data.UserData
import com.milord.coursework.databinding.FragmentHistoryBinding
import com.milord.coursework.utils.payments.PaymentAdapter
import com.milord.coursework.data.UserViewModel

class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private val userViewModel = UserViewModel.getInstance()
    private var user : UserData? = null
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
        user = userViewModel.userData.value
        historyList = binding.historyListView
        val filterButton = binding.filterButton
        paymentList = user!!.getPayments()
        paymentAdapter = PaymentAdapter(paymentList)

        userViewModel.userData.observe(viewLifecycleOwner) { userData ->
            user = userData
            update(user!!.getPayments())
        }

        filterButton.setOnClickListener {
            val dialog = DatePickerDialog(requireContext())
            dialog.show()
            dialog.setOnDateSetListener { view, year, month, dayOfMonth ->
                val date = "$dayOfMonth.${month+1}.$year"
                val filteredList =
                    user!!.getPayments().filter { it.date == date } as ArrayList<Payment>
                update(filteredList)
            }
        }
        historyList.layoutManager = LinearLayoutManager(context)
        historyList.setHasFixedSize(false)
        historyList.adapter = paymentAdapter
    }

    private fun update(newList:ArrayList<Payment>){
        paymentList = newList
        paymentAdapter = PaymentAdapter(paymentList)
        historyList.adapter = paymentAdapter
    }
}