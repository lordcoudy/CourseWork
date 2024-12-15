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
            builder.setTitle(getString(R.string.not_enough_money))
                .setMessage(getString(R.string.please_pay))
                .setIcon(R.mipmap.exclamation)
                .setPositiveButton(getString(R.string.ok)) {
                        dialog, _ ->  dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}