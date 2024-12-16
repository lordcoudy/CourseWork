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
import com.milord.coursework.data.UserData
import com.milord.coursework.databinding.FragmentPaymentBinding
import com.milord.coursework.data.UserViewModel
import com.milord.coursework.data.prefs.SaveSharedPreference
import com.milord.coursework.utils.api.ApiClient
import com.milord.coursework.utils.api.BalanceResponse
import com.milord.coursework.utils.api.GetReadingsRequestArr
import com.milord.coursework.utils.api.PaymentExt
import com.milord.coursework.utils.api.ReadingType
import com.milord.coursework.utils.api.TopUpRequest
import com.milord.coursework.utils.api.UserHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentFragment : Fragment()
{
    private lateinit var binding : FragmentPaymentBinding
    private val userViewModel = UserViewModel.getInstance()
    private var user : UserData = UserData()
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

        user = userViewModel.userData.value!!

        userViewModel.userData.observe(viewLifecycleOwner) { userData ->
            user = userData
            INNTw.text = user.getINN()
            currentBalance.text = buildString {
                append(getString(R.string.balance))
                append(user.getBalance().balance.toString())
            }
        }

        buttonPay.setOnClickListener {
            topUp(addedBalanceEt.text.toString().toDouble())
            findNavController().navigate(R.id.action_paymentFragment_to_navigation_home)
        }

        buttonBack.setOnClickListener {
            findNavController().navigate(R.id.action_paymentFragment_to_navigation_home)
        }
    }

    private fun updateBalance()
    {
        apiClient.getApiService()
            .getBalance(token = "Bearer ${SaveSharedPreference(requireContext()).getToken()}")
            .enqueue(object : Callback<BalanceResponse>
            {
                override fun onFailure(call: Call<BalanceResponse>, t: Throwable)
                {
                    Toast.makeText(context, getString(R.string.something_went_wrong, t.message), Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<BalanceResponse>,
                    response: Response<BalanceResponse>
                )
                {
                    val balanceResponse = response.body()

                    if (response.code() == 200)
                    {
                        try
                        {
                            val balance = user.getBalance()
                            balance.balance = balanceResponse!!.balance.toDouble()
                            user.setBalance(balance)
                            userViewModel.updateUser(user)
                            updateReadings()
                        }
                        catch (e: NullPointerException)
                        {
                            Toast.makeText(
                                context,
                                getString(R.string.balance_error, response.message()),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    else
                    {
                        Toast.makeText(
                            context,
                            getString(R.string.balance_error, response.message()),
                            Toast.LENGTH_SHORT
                        ).show()
                        requireActivity().startActivityFromFragment(
                            this@PaymentFragment,
                            Intent(requireActivity(), AuthActivity::class.java),
                            0
                        )
                        requireActivity().finish()
                    }
                }

            })
    }

    private fun updateReadings()
    {
        apiClient.getApiService()
            .getLastReadings(token = "Bearer ${SaveSharedPreference(requireContext()).getToken()}")
            .enqueue(object : Callback<GetReadingsRequestArr>
            {
                override fun onFailure(call: Call<GetReadingsRequestArr>, t: Throwable)
                {
                    Toast.makeText(context, getString(R.string.something_went_wrong, t.message), Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<GetReadingsRequestArr>,
                    response: Response<GetReadingsRequestArr>
                )
                {
                    val readingsResponse = response.body()

                    if (response.code() == 200)
                    {
                        try
                        {
                            val balance = user.getBalance()
                            for (reading in readingsResponse!!.readings)
                            {
                                when (reading.type)
                                {
                                    ReadingType.UNKNOWN.ordinal -> Toast.makeText(
                                        context,
                                        getString(R.string.unknown_type),
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    ReadingType.ELECTRICITY.ordinal -> balance.electricity =
                                        reading.value

                                    ReadingType.COLD_WATER.ordinal -> balance.coldWater =
                                        reading.value

                                    ReadingType.HOT_WATER.ordinal -> balance.hotWater =
                                        reading.value

                                    ReadingType.HOUSE_HEATING.ordinal -> balance.houseHeating =
                                        reading.value

                                    ReadingType.MAINTENANCE.ordinal -> balance.maintenance =
                                        reading.value
                                }
                            }
                            userViewModel.updateUser(user)
                            updatePayments()
                        }
                        catch (e: NullPointerException)
                        {
                            Toast.makeText(
                                context,
                                getString(R.string.data_loading_error, response.message()),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    else
                    {
                        Toast.makeText(
                            context,
                            getString(R.string.data_loading_error, response.message()),
                            Toast.LENGTH_SHORT
                        ).show()
                        requireActivity().startActivityFromFragment(
                            this@PaymentFragment,
                            Intent(requireActivity(), AuthActivity::class.java),
                            0
                        )
                        requireActivity().finish()
                    }
                }

            })
    }

    private fun updatePayments()
    {
        apiClient.getApiService()
            .getPayments(token = "Bearer ${SaveSharedPreference(requireContext()).getToken()}")
            .enqueue(object : Callback<PaymentExt>
            {
                override fun onFailure(call: Call<PaymentExt>, t: Throwable)
                {
                    Toast.makeText(context, getString(R.string.something_went_wrong, t.message), Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<PaymentExt>,
                    response: Response<PaymentExt>
                )
                {
                    val payments = response.body()

                    if (payments != null)
                    {
                        try
                        {
                            user.setPayments(payments.payments)
                            userViewModel.updateUser(user)
                            user = userViewModel.userData.value!!
                        }
                        catch (e: NullPointerException)
                        {
                            Toast.makeText(
                                context,
                                getString(R.string.payments_loading_error, response.message()),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    else
                    {
                        Toast.makeText(
                            context,
                            getString(R.string.payments_loading_error),
                            Toast.LENGTH_SHORT
                        ).show()
                        requireActivity().startActivityFromFragment(
                            this@PaymentFragment,
                            Intent(requireActivity(), AuthActivity::class.java),
                            0
                        )
                        requireActivity().finish()
                    }
                }

            })
    }

    private fun updateUser()
    {
        apiClient.getApiService()
            .getUser(token = "Bearer ${SaveSharedPreference(requireContext()).getToken()}")
            .enqueue(object : Callback<UserHelper>
            {
                override fun onFailure(call: Call<UserHelper>, t: Throwable)
                {
                    Toast.makeText(context, getString(R.string.something_went_wrong, t.message), Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<UserHelper>,
                    response: Response<UserHelper>
                )
                {
                    val userHelper = response.body()

                    if (response.code() == 200)
                    {
                        try
                        {
                            user.setName(userHelper!!.name)
                            user.setINN(userHelper.INN)
                            user.setAddress(userHelper.address)
                            user.setEmail(userHelper.email)
                            userViewModel.updateUser(user)
                            updateBalance()
                        }
                        catch (e: NullPointerException)
                        {
                            Toast.makeText(
                                context,
                                getString(R.string.user_loading_error, response.message()),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    else
                    {
                        Toast.makeText(
                            context,
                            getString(R.string.user_loading_error, response.message()),
                            Toast.LENGTH_SHORT
                        ).show()
                        requireActivity().startActivityFromFragment(
                            this@PaymentFragment,
                            Intent(requireActivity(), AuthActivity::class.java),
                            0
                        )
                        requireActivity().finish()
                    }
                }
            })
    }

    private fun updateAll()
    {
        updateUser()
    }

    private fun topUp(amount : Double)
    {
        apiClient.getApiService()
            .topUp(
                token = "Bearer ${SaveSharedPreference(requireContext()).getToken()}",
                request = TopUpRequest(amount)
            )
            .enqueue(object : Callback<Void>
            {
                override fun onFailure(call: Call<Void>, t: Throwable)
                {
                    Toast.makeText(context, getString(R.string.something_went_wrong, t.message), Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<Void>,
                    response: Response<Void>
                )
                {
                    if (response.code() == 200)
                    {
                        Toast.makeText(context,
                            getString(R.string.successful_payment), Toast.LENGTH_SHORT).show()
                        updateAll()
                    }
                    else
                    {
                        Toast.makeText(
                            context,
                            getString(R.string.payment_error, response.message()),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
    }
}