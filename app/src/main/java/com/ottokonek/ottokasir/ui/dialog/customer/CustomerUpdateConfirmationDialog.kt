package com.ottokonek.ottokasir.ui.dialog.customer

import android.content.Context
import android.os.Bundle
import androidx.annotation.NonNull
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ottokonek.ottokasir.R
import kotlinx.android.synthetic.main.dialog_confirmation_update_customer.*

class CustomerUpdateConfirmationDialog(@NonNull context: Context, style: Int, private val callbackCustomerUpdate: CustomerUpdateCallback) : BottomSheetDialog(context, style) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_confirmation_update_customer)
        setCancelable(false)

        btnYes.setOnClickListener {
            this.dismiss()
            callbackCustomerUpdate.onCustomerUpdateYes()
        }

        btnCancel.setOnClickListener {
            this.dismiss()
        }

    }

    override fun onBackPressed() {
        this.dismiss()
        super.onBackPressed()
    }
}