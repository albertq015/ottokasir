package com.ottokonek.ottokasir.ui.dialog.loading

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.ottokonek.ottokasir.R
import java.lang.Exception

class CustomProgressDialog(context: Context) : Dialog(context) {

    companion object {
        val TAG = CustomProgressDialog::class.java.simpleName
        private var dialog: CustomProgressDialog? = null

        fun showDialog(context: Context?, message: String) {
            if (context == null) return

            try {
                if (dialog == null)
                    dialog = CustomProgressDialog(context)

                if (!dialog!!.isShowing)
                    dialog!!.show()
            } catch (e: Exception) {
                Log.e(TAG, e.message ?: "")
            }
        }

        fun closeDialog() {
            try {
                dialog?.dismiss()

                // remove dialog from memory
                dialog = null
            } catch (e: Exception) {
                Log.e(TAG, e.message ?: "")
            }
        }
    }

    override fun onStart() {
        super.onStart()

        // configure dialog
        setCancelable(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_custom_progress)
        setCancelable(false)
    }

}