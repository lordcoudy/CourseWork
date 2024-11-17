package com.milord.coursework.utils

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.milord.coursework.R

class NotificationDialogFragment : DialogFragment()
{
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
    {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Недостаточно средств на балансе!")
                .setMessage("Для оплаты задолженности ЖКХ не хватает средств на счёте! Пополните счёт в ближайшее время.")
                .setIcon(R.mipmap.exclamation)
                .setPositiveButton("ОК") {
                        dialog, id ->  dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}