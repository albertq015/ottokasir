package com.ottokonek.ottokasir.utils

import android.content.Context
import android.widget.Toast
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.ui.dialog.MessageDialog

open class MessageUserUtil{

    companion object{

        fun emptyInput(context: Context) = toastMessage(context, R.string.error_empty_input)

        fun underConstruction(context: Context) = toastMessage(context, R.string.default_notif)

        fun default(context: Context?, message: String) = dialogMessage(context, message)

        fun default(context: Context?, message: String, callback: MessageDialog.ActionMessage) = dialogMessage(context, message, callback)

        fun shortMessage(context: Context, message: Int) = toastMessage(context, message)

        fun shortMessage(context: Context, message: String) = toastMessage(context, message)

        /*
            all type of message view
         */

        fun dialogMessage(context: Context?, message: String) = context?.let {
            MessageDialog.showMessage(it, message)
        }

        fun dialogMessage(context: Context?, message: String, callback: MessageDialog.ActionMessage) = context?.let {
            MessageDialog.showMessage(it, message, callback)
        }

        fun dialogMessage(context: Context?) = context?.let {
            MessageDialog.showMessage(it, it.getString(R.string.error_global))
        }

        fun toastMessage(context: Context, message: Int) = Toast.makeText(context, context.getString(message), Toast.LENGTH_SHORT).show()

        fun toastMessage(context: Context, message: String) = Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

        fun toastMessage(context: Context) = Toast.makeText(context, context.getString(R.string.error_global), Toast.LENGTH_SHORT).show()
    }
}