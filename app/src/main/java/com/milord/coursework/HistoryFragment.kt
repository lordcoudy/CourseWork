package com.milord.coursework

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.milord.coursework.data.Payment
import com.milord.coursework.data.UserData
import com.milord.coursework.databinding.FragmentHistoryBinding
import com.milord.coursework.utils.PaymentAdapter
import com.milord.coursework.utils.UserViewModel

class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private val userViewModel = UserViewModel.getInstance()
    private var user : UserData? = null
    private lateinit var paymentList : ArrayList<Payment>
    private lateinit var paymentAdapter : PaymentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(layoutInflater)
        paymentList = ArrayList()
        paymentAdapter = PaymentAdapter(paymentList)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        val historyList = binding.historyListView
        val historyButton = binding.updateHistoryButton

        userViewModel.userData.observe(viewLifecycleOwner) { userData ->
            user = userData
            update(user!!.getPayments())
        }

        historyButton.setOnClickListener {
            update(user!!.getPayments())
        }

        historyList.layoutManager = LinearLayoutManager(context)
        historyList.setHasFixedSize(true)
        historyList.adapter = paymentAdapter
    }

    private fun update(newList:ArrayList<Payment>){
        paymentList = newList
        paymentAdapter.notifyDataSetChanged()
    }
}