package com.milord.coursework.main

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import com.milord.coursework.R
import com.milord.coursework.auth.AuthActivity
import com.milord.coursework.data.UserData
import com.milord.coursework.data.UserViewModel
import com.milord.coursework.data.prefs.SaveSharedPreference
import com.milord.coursework.databinding.FragmentMainBinding
import com.milord.coursework.utils.NotificationDialogFragment
import com.milord.coursework.utils.api.ApiClient
import com.milord.coursework.utils.api.BalanceResponse
import com.milord.coursework.utils.api.GetReadingsRequestArr
import com.milord.coursework.utils.api.PaymentExt
import com.milord.coursework.utils.api.ReadingType
import com.milord.coursework.utils.api.UserHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainFragment : Fragment()
{
    private lateinit var binding: FragmentMainBinding
    private val userViewModel = UserViewModel.getInstance()
    private var user: UserData? = null
    private lateinit var apiClient: ApiClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View
    {
        binding = FragmentMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        apiClient = ApiClient()
        user = UserData("", "", "", "")
        updateAll()

        val balanceButton = binding.balanceButton
        val electricityText = binding.electricityText
        val coldWaterText = binding.coldWaterText
        val hotWaterText = binding.hotWaterText
        val houseHeatingText = binding.houseHeatingText
        val maintenanceText = binding.maintenanceText
        val electricityButton = binding.editElectricityButton
        val coldWaterButton = binding.editColdWaterButton
        val hotWaterButton = binding.editHotWaterButton
        val houseHeatingButton = binding.editHouseHeatingButton
        val maintenanceButton = binding.editMaintenanceButton
        val balanceUpdate = binding.refreshBalanceButton
        val notification = binding.BalanceIcon
        val INN = binding.INNTw
        val updateAllButton = binding.updateAllButton

        updateAllButton.setOnClickListener()
        {
            apiClient.getApiService()
                .storeReadings(
                    token = "Bearer ${SaveSharedPreference(requireContext()).getToken()}",
                    user!!.getBalance().getReadings()
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
                                getString(R.string.data_sent), Toast.LENGTH_SHORT).show()
                            binding.updateAllButton.visibility = View.GONE
                            updateBalance()
                        }
                        else
                        {
                            Toast.makeText(
                                context,
                                getString(R.string.data_sending_error, response.message()),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
        }

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
            houseHeatingText.text = buildString {
                append(getString(R.string.heating))
                append(user!!.getBalance().houseHeating.toString())
            }
            maintenanceText.text = buildString {
                append(getString(R.string.maintenance))
                append(user!!.getBalance().maintenance.toString())
            }
            if (user!!.getBalance().balance < userData!!.getBalance().cap && notification.visibility == View.GONE)
            {
                showNotificationDialog()
                notification.visibility = View.VISIBLE
            }
        }

        electricityButton.setOnClickListener()
        {
            val alertDialog = AlertDialog.Builder(context)
            val edittext = EditText(context)
            alertDialog.setMessage(getString(R.string.edit_data))
            alertDialog.setTitle(getString(R.string.electricity))

            alertDialog.setView(edittext)

            alertDialog.setPositiveButton(
                getString(R.string.enter)
            ) { _, _ -> //What ever you want to do with the value
                val balance = user?.getBalance()
                balance!!.electricity = edittext.text.toString().toDouble()
                user?.setBalance(balance)
                setData(ReadingType.ELECTRICITY)
                loadData(ReadingType.ELECTRICITY)
            }

            alertDialog.setNegativeButton(
                getString(R.string.cancel)
            ) { _, _ ->
            }

            alertDialog.show()
        }
        coldWaterButton.setOnClickListener()
        {
            val alertDialog = AlertDialog.Builder(context)
            val edittext = EditText(context)
            alertDialog.setMessage(getString(R.string.edit_data))
            alertDialog.setTitle(getString(R.string.cold_water))

            alertDialog.setView(edittext)

            alertDialog.setPositiveButton(
                getString(R.string.enter)
            ) { _, _ -> //What ever you want to do with the value
                val balance = user?.getBalance()
                balance!!.coldWater = edittext.text.toString().toDouble()
                user?.setBalance(balance)
                setData(ReadingType.COLD_WATER)
                loadData(ReadingType.COLD_WATER)
            }

            alertDialog.setNegativeButton(
                getString(R.string.cancel)
            ) { _, _ ->
            }

            alertDialog.show()
        }
        hotWaterButton.setOnClickListener()
        {
            val alertDialog = AlertDialog.Builder(context)
            val edittext = EditText(context)
            alertDialog.setMessage(getString(R.string.edit_data))
            alertDialog.setTitle(getString(R.string.hot_water))

            alertDialog.setView(edittext)

            alertDialog.setPositiveButton(
                getString(R.string.enter)
            ) { _, _ -> //What ever you want to do with the value
                val balance = user?.getBalance()
                balance!!.hotWater = edittext.text.toString().toDouble()
                user?.setBalance(balance)
                setData(ReadingType.HOT_WATER)
                loadData(ReadingType.HOT_WATER)
            }

            alertDialog.setNegativeButton(
                getString(R.string.cancel)
            ) { _, _ ->
            }

            alertDialog.show()
        }
        houseHeatingButton.setOnClickListener()
        {
            val alertDialog = AlertDialog.Builder(context)
            val edittext = EditText(context)
            alertDialog.setMessage(getString(R.string.edit_data))
            alertDialog.setTitle(getString(R.string.heating))

            alertDialog.setView(edittext)

            alertDialog.setPositiveButton(
                getString(R.string.enter)
            ) { _, _ -> //What ever you want to do with the value
                val balance = user?.getBalance()
                balance!!.houseHeating = edittext.text.toString().toDouble()
                user?.setBalance(balance)
                setData(ReadingType.HOUSE_HEATING)
                loadData(ReadingType.HOUSE_HEATING)
            }

            alertDialog.setNegativeButton(
                getString(R.string.cancel)
            ) { _, _ ->
            }

            alertDialog.show()
        }
        maintenanceButton.setOnClickListener()
        {
            val alertDialog = AlertDialog.Builder(context)
            val edittext = EditText(context)
            alertDialog.setMessage(getString(R.string.edit_data))
            alertDialog.setTitle(getString(R.string.maintenance))

            alertDialog.setView(edittext)

            alertDialog.setPositiveButton(
                getString(R.string.enter)
            ) { _, _ -> //What ever you want to do with the value
                val balance = user?.getBalance()
                balance!!.maintenance = edittext.text.toString().toDouble()
                user?.setBalance(balance)
                setData(ReadingType.MAINTENANCE)
                loadData(ReadingType.MAINTENANCE)
            }

            alertDialog.setNegativeButton(
                getString(R.string.cancel),
                DialogInterface.OnClickListener { _, _ ->
                    // what ever you want to do with No option.
                })

            alertDialog.show()
        }
        balanceUpdate.setOnClickListener()
        {
            updateAll()
            try
            {
                binding.balanceButton.text = buildString {
                    append(getString(R.string.balance))
                    append(user!!.getBalance().balance.toString())
                }
                if (user!!.getBalance().balance < user!!.getBalance().cap && notification.visibility == View.GONE)
                {
                    showNotificationDialog()
                    notification.visibility = View.VISIBLE
                }
            }
            catch (e: NullPointerException)
            {
                Toast.makeText(context, getString(R.string.balance_error, "null"), Toast.LENGTH_SHORT)
                    .show()
            }
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
                            val balance = user!!.getBalance()
                            balance.balance = balanceResponse!!.balance.toDouble()
                            user!!.setBalance(balance)
                            userViewModel.updateUser(user!!)
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
                            this@MainFragment,
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
                            val balance = user!!.getBalance()
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
                            userViewModel.updateUser(user!!)
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
                            this@MainFragment,
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

                    if (response.code() == 200)
                    {
                        try
                        {
                            user?.setPayments(payments!!.payments)
                            userViewModel.updateUser(user!!)
                            user = userViewModel.userData.value
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
                            this@MainFragment,
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
                            user!!.setName(userHelper!!.name)
                            user!!.setINN(userHelper.INN)
                            user!!.setAddress(userHelper.address)
                            user!!.setEmail(userHelper.email)
                            userViewModel.updateUser(user!!)
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
                            this@MainFragment,
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

    private fun setData(data : ReadingType)
    {
        when (data)
        {
            ReadingType.ELECTRICITY ->
            {
                binding.electricityText.text = buildString {
                    append(getString(R.string.electricity))
                    append(user!!.getBalance().electricity.toString())
                }
            }

            ReadingType.COLD_WATER ->
            {
                binding.coldWaterText.text = buildString {
                    append(getString(R.string.cold_water))
                    append(user!!.getBalance().coldWater.toString())
                }
            }

            ReadingType.HOT_WATER ->
            {
                binding.hotWaterText.text = buildString {
                    append(getString(R.string.hot_water))
                    append(user!!.getBalance().hotWater.toString())
                }
            }

            ReadingType.HOUSE_HEATING ->
            {
                binding.houseHeatingText.text = buildString {
                    append(getString(R.string.heating))
                    append(user!!.getBalance().houseHeating.toString())
                }
            }
            ReadingType.MAINTENANCE ->
            {
                binding.maintenanceText.text = buildString {
                    append(getString(R.string.maintenance))
                    append(user!!.getBalance().maintenance.toString())
                }
            }
            ReadingType.UNKNOWN ->
            {
                Toast.makeText(context, getString(R.string.unknown_type), Toast.LENGTH_SHORT).show()
            }
        }
        binding.updateAllButton.visibility = View.VISIBLE
    }

    private fun loadData(data : ReadingType)
    {
        when (data)
        {
            ReadingType.ELECTRICITY ->
            {
                user?.getBalance()?.electricity = binding.electricityText.text.toString().subSequence(getString(R.string.electricity).length, binding.electricityText.text.length).toString().toDouble()
                userViewModel.updateUser(user!!)
            }

            ReadingType.COLD_WATER ->
            {
                user?.getBalance()?.coldWater = binding.coldWaterText.text.toString().subSequence(getString(R.string.cold_water).length, binding.coldWaterText.text.length).toString().toDouble()
                userViewModel.updateUser(user!!)
            }

            ReadingType.HOT_WATER ->
            {
                user?.getBalance()?.hotWater = binding.hotWaterText.text.toString().subSequence(getString(R.string.hot_water).length, binding.hotWaterText.text.length).toString().toDouble()
                userViewModel.updateUser(user!!)
            }

            ReadingType.HOUSE_HEATING ->
            {
                user?.getBalance()?.houseHeating = binding.houseHeatingText.text.toString().subSequence(getString(R.string.heating).length, binding.houseHeatingText.text.length).toString().toDouble()
                userViewModel.updateUser(user!!)
            }
            ReadingType.MAINTENANCE ->
            {
                user?.getBalance()?.maintenance = binding.maintenanceText.text.toString().subSequence(getString(R.string.maintenance).length, binding.maintenanceText.text.length).toString().toDouble()
                userViewModel.updateUser(user!!)
            }
            ReadingType.UNKNOWN ->
            {
                Toast.makeText(context, getString(R.string.unknown_type), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showNotificationDialog()
    {
        val myDialogFragment = NotificationDialogFragment()
        val manager = requireActivity().supportFragmentManager
        val transaction: FragmentTransaction = manager.beginTransaction()
        myDialogFragment.show(transaction, "dialog")
    }
}