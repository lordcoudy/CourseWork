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
import com.milord.coursework.data.Payment
import com.milord.coursework.data.UserData
import com.milord.coursework.data.UserViewModel
import com.milord.coursework.data.prefs.SaveSharedPreference
import com.milord.coursework.databinding.FragmentMainBinding
import com.milord.coursework.utils.NotificationDialogFragment
import com.milord.coursework.utils.api.ApiClient
import com.milord.coursework.utils.api.BalanceResponse
import com.milord.coursework.utils.api.ReadingType
import com.milord.coursework.utils.api.StoreReadingsRequest
import com.milord.coursework.utils.api.StoreReadingsResponse
import com.milord.coursework.utils.api.UserHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


enum class Data
{
    Electricity,
    ColdWater,
    HotWater,
    HouseHeating,
    Maintenance,
    Balance
}

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

        user = userViewModel.userData.value
        updateAll()
        user = userViewModel.userData.value

        val balanceButton = binding.balanceButton
        val electricityText = binding.electricityText
        val coldWaterText = binding.coldWaterText
        val hotWaterText = binding.hotWaterText
        val electricityButton = binding.editElectricityButton
        val coldWaterButton = binding.editColdWaterButton
        val hotWaterButton = binding.editHotWaterButton
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
                .enqueue(object : Callback<StoreReadingsResponse>
                {
                    override fun onFailure(call: Call<StoreReadingsResponse>, t: Throwable)
                    {
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<StoreReadingsResponse>,
                        response: Response<StoreReadingsResponse>
                    )
                    {
                        val storeReadingsResponse = response.body()

                        if (storeReadingsResponse?.status == null)
                        {
                            Toast.makeText(context, "Данные переданы!", Toast.LENGTH_SHORT).show()
                            binding.updateAllButton.visibility = View.GONE
                        }
                        else
                        {
                            Toast.makeText(
                                context,
                                "Ошибка при передаче данных: ${storeReadingsResponse.status}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
            updateReadings()
            updateBalance()
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
            if (user!!.getBalance().balance < userData.getBalance().cap)
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
                getString(R.string.enter),
                DialogInterface.OnClickListener { dialog, whichButton -> //What ever you want to do with the value
                    val balance = user?.getBalance()
                    balance!!.electricity = edittext.text.toString().toDouble()
                    user?.setBalance(balance)
                    setData(Data.Electricity)
                    loadData(Data.Electricity)
                })

            alertDialog.setNegativeButton(
                getString(R.string.cancel),
                DialogInterface.OnClickListener { dialog, whichButton ->
                })

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
                getString(R.string.enter),
                DialogInterface.OnClickListener { dialog, whichButton -> //What ever you want to do with the value
                    val balance = user?.getBalance()
                    balance!!.coldWater = edittext.text.toString().toDouble()
                    user?.setBalance(balance)
                    setData(Data.ColdWater)
                    loadData(Data.ColdWater)
                })

            alertDialog.setNegativeButton(
                getString(R.string.cancel),
                DialogInterface.OnClickListener { dialog, whichButton ->
                    // what ever you want to do with No option.
                })

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
                getString(R.string.enter),
                DialogInterface.OnClickListener { dialog, whichButton -> //What ever you want to do with the value
                    val balance = user?.getBalance()
                    balance!!.hotWater = edittext.text.toString().toDouble()
                    user?.setBalance(balance)
                    setData(Data.HotWater)
                    loadData(Data.HotWater)
                })

            alertDialog.setNegativeButton(
                getString(R.string.cancel),
                DialogInterface.OnClickListener { dialog, whichButton ->
                    // what ever you want to do with No option.
                })

            alertDialog.show()
        }
        balanceUpdate.setOnClickListener()
        {
            loadData(Data.Balance)
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
                        val balance = user?.getBalance()
                        balance?.balance = balanceResponse.balance.toDouble()
                        userViewModel.updateUser(user!!)
                    }
                    else
                    {
                        Toast.makeText(
                            context,
                            "Ошибка при загрузке пользователя",
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
            .enqueue(object : Callback<ArrayList<StoreReadingsRequest>>
            {
                override fun onFailure(call: Call<ArrayList<StoreReadingsRequest>>, t: Throwable)
                {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<ArrayList<StoreReadingsRequest>>,
                    response: Response<ArrayList<StoreReadingsRequest>>
                )
                {
                    val readingsResponse = response.body()

                    if (readingsResponse != null)
                    {
                        user = userViewModel.userData.value
                        val balance = user!!.getBalance()
                        for (reading in readingsResponse)
                        {
                            when (reading.type)
                            {
                                ReadingType.UNKNOWN.ordinal -> TODO()
                                ReadingType.ELECTRICITY.ordinal -> balance.electricity = reading.value
                                ReadingType.COLD_WATER.ordinal -> balance.coldWater = reading.value
                                ReadingType.HOT_WATER.ordinal -> balance.hotWater = reading.value
                                ReadingType.HOUSE_HEATING.ordinal -> balance.houseHeating = reading.value
                                ReadingType.MAINTENANCE.ordinal -> balance.maintenance = reading.value
                            }
                        }
                        userViewModel.updateUser(user!!)
                    }
                    else
                    {
                        Toast.makeText(
                            context,
                            "Ошибка при загрузке пользователя",
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
                        user = userViewModel.userData.value
                        user?.setPayments(payments)
                        userViewModel.updateUser(user!!)
                    }
                    else
                    {
                        Toast.makeText(
                            context,
                            "Ошибка при загрузке пользователя",
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
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<UserHelper>,
                    response: Response<UserHelper>
                )
                {
                    val userHelper = response.body()

                    if (userHelper != null)
                    {
                        user = userViewModel.userData.value
                        user = UserData(
                            userHelper.email,
                            userHelper.INN,
                            userHelper.address,
                            userHelper.name
                        )
                        userViewModel.updateUser(user!!)
                    }
                    else
                    {
                        Toast.makeText(
                            context,
                            "Ошибка при загрузке пользователя",
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
        updateBalance()
        updateReadings()
        updatePayments()
    }

    private fun setData(data : Data)
    {
        when (data)
        {
            Data.Electricity ->
            {
                binding.electricityText.text = buildString {
                    append(getString(R.string.electricity))
                    append(user!!.getBalance().electricity.toString())
                }
            }

            Data.ColdWater ->
            {
                binding.coldWaterText.text = buildString {
                    append(getString(R.string.cold_water))
                    append(user!!.getBalance().coldWater.toString())
                }
            }

            Data.HotWater ->
            {
                binding.hotWaterText.text = buildString {
                    append(getString(R.string.hot_water))
                    append(user!!.getBalance().hotWater.toString())
                }
            }

            Data.Balance ->
            {
                binding.balanceButton.text = buildString {
                    append(getString(R.string.balance))
                    append(user!!.getBalance().balance.toString())
                }
            }

            Data.HouseHeating ->
            {
                TODO()
            }
            Data.Maintenance ->
            {
                TODO()
            }
        }
        binding.updateAllButton.visibility = View.VISIBLE
    }

    private fun loadData(data : Data)
    {
        when (data)
        {
            Data.Electricity ->
            {
                user?.getBalance()?.electricity = binding.electricityText.text.toString().subSequence(15, binding.electricityText.text.length).toString().toDouble()
                userViewModel.updateUser(user!!)
            }

            Data.ColdWater ->
            {
                user?.getBalance()?.coldWater = binding.coldWaterText.text.toString().subSequence(5, binding.coldWaterText.text.length).toString().toDouble()
                userViewModel.updateUser(user!!)
            }

            Data.HotWater ->
            {
                user?.getBalance()?.hotWater = binding.hotWaterText.text.toString().subSequence(5, binding.hotWaterText.text.length).toString().toDouble()
                userViewModel.updateUser(user!!)
            }

            Data.Balance ->
            {
                user?.getBalance()?.balance = binding.balanceButton.text.toString().subSequence(8, binding.balanceButton.text.length).toString().toDouble()
                userViewModel.updateUser(user!!)
            }

            Data.HouseHeating -> TODO()
            Data.Maintenance -> TODO()
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