package com.milord.coursework.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.milord.coursework.R
import com.milord.coursework.auth.AuthActivity
import com.milord.coursework.data.Payment
import com.milord.coursework.data.UserData
import com.milord.coursework.databinding.FragmentPaymentBinding
import com.milord.coursework.data.UserViewModel
import com.milord.coursework.data.prefs.SaveSharedPreference
import com.milord.coursework.utils.api.ApiClient
import com.milord.coursework.utils.api.BalanceResponse
import com.milord.coursework.utils.api.StoreReadingsResponse
import com.milord.coursework.utils.api.TopUpRequest
import com.milord.coursework.utils.api.TopUpResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentFragment : Fragment()
{
    private lateinit var binding : FragmentPaymentBinding
    private val userViewModel = UserViewModel.getInstance()
    private var user : UserData? = null
    private lateinit var apiClient: ApiClient

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
        apiClient = ApiClient()

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
            topUp(addedBalanceEt.text.toString().toDouble())
            updatePayments()
            updateBalance()
            findNavController().navigate(R.id.action_paymentFragment_to_navigation_home)
        }

        buttonBack.setOnClickListener {
            findNavController().navigate(R.id.action_paymentFragment_to_navigation_home)
        }
    }

    private fun updateBalance()
    {
        apiClient.getApiService()
            .getBalance(token = "Bearer ${user!!.getToken()}")
            .enqueue(object : Callback<BalanceResponse>
            {
                override fun onFailure(call: Call<BalanceResponse>, t: Throwable)
                {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<BalanceResponse>,
                    response: Response<BalanceResponse>
                )
                {
                    val balanceResponse = response.body()

                    if (balanceResponse != null)
                    {
                        user = userViewModel.userData.value
                        val balance = user!!.getBalance()
                        balance.balance = balanceResponse.balance.toDouble()
                        userViewModel.updateUser(user!!)
                    }
                    else
                    {
                        Toast.makeText(
                            context,
                            "Ошибка при пополнении",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            })
    }

    private fun updatePayments()
    {
        apiClient.getApiService()
            .getPayments(token = "Bearer ${SaveSharedPreference(requireContext()).getToken()}")
            .enqueue(object : Callback<ArrayList<Payment>>
            {
                override fun onFailure(call: Call<ArrayList<Payment>>, t: Throwable)
                {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<ArrayList<Payment>>,
                    response: Response<ArrayList<Payment>>
                )
                {
                    val payments = response.body()

                    if (payments != null)
                    {
                        user!!.setPayments(payments)
                        userViewModel.updateUser(user!!)
                    }
                    else
                    {
                        Toast.makeText(
                            context,
                            "Ошибка при загрузке пользователя",
                            Toast.LENGTH_SHORT
                        ).show()
                        findNavController().navigate(R.id.action_paymentFragment_to_navigation_home)
                    }
                }

            })
    }

    fun topUp(amount : Double)
    {
        apiClient.getApiService()
            .topUp(
                token = "Bearer ${user!!.getToken()}",
                request = TopUpRequest(amount)
            )
            .enqueue(object : Callback<TopUpResponse>
            {
                override fun onFailure(call: Call<TopUpResponse>, t: Throwable)
                {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<TopUpResponse>,
                    response: Response<TopUpResponse>
                )
                {
                    val topUpResponse = response.body()

                    if (topUpResponse?.status == null)
                    {
                        Toast.makeText(context, "Платёж успешный!", Toast.LENGTH_SHORT).show()
                    }
                    else
                    {
                        Toast.makeText(
                            context,
                            "Ошибка при платеже: ${topUpResponse.status}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
    }
}