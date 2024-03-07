package com.ottokonek.ottokasir.ui.dialog.customer

import android.content.Context
import android.os.Bundle
import androidx.annotation.NonNull
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ottokonek.ottokasir.R
import kotlinx.android.synthetic.main.dialog_added_customer.*

class CustomerCreateDialog(@NonNull context: Context, style: Int, private val callbackCreateCustomer: CustomerCreateCallback) : BottomSheetDialog(context, style) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_added_customer)
        setCancelable(false)

        btnOk.setOnClickListener {
            this.dismiss()
            successAddedCustomer()
        }
    }

    private fun successAddedCustomer() {
        this.dismiss()
        callbackCreateCustomer.onBackToListCustomer()
    }

    override fun onBackPressed() {
        successAddedCustomer()
        super.onBackPressed()
    }
}