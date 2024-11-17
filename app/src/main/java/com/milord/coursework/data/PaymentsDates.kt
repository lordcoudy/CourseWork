package com.milord.coursework.data

import android.icu.util.Calendar
import java.text.SimpleDateFormat
import java.util.Locale

val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().time)
data class PaymentsDates(var electricityDate : String = currentDate, var coldWaterDate : String = currentDate, var hotWaterDate : String = currentDate)
