package com.milord.coursework.utils.payments

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.milord.coursework.R

class PaymentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
{
    val date : TextView = itemView.findViewById(R.id.paymentItemDate)
    val sum : TextView = itemView.findViewById(R.id.paymentItemSum)
}