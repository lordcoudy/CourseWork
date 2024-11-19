package com.milord.coursework.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.milord.coursework.R
import com.milord.coursework.data.Payment

class PaymentAdapter (private val payments: List<Payment>) : RecyclerView.Adapter<PaymentViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder
    {
        val viewLayout = LayoutInflater.from(parent.context).inflate(
            R.layout.payment_item,
            parent,false)
        return PaymentViewHolder(viewLayout)
    }

    override fun getItemCount() = payments.size
    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int)
    {
        val currentPayment = payments[position]
        holder.date.text = currentPayment.date
        holder.sum.text = currentPayment.sum
    }
}

class PaymentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
{
    val date : TextView = itemView.findViewById(R.id.paymentItemDate)
    val sum : TextView = itemView.findViewById(R.id.paymentItemSum)
}