package com.ottokonek.ottokasir.ui.dialog.customer

import android.content.Context
import android.os.Bundle
import androidx.annotation.NonNull
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ottokonek.ottokasir.R
import kotlinx.android.synthetic.main.dialog_success_delete_customer.*

class CustomerDeleteSuccessDialog(@NonNull context: Context, style: Int, private val callbackCustomerDelete: CustomerDeleteCallback) : BottomSheetDialog(context, style) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_success_delete_customer)
        setCancelable(false)

        btnOk.setOnClickListener {
            this.dismiss()
            successResetStoreType()
        }
    }

    private fun successResetStoreType() {
        this.dismiss()
        callbackCustomerDelete.onCustomerDeleteSuccess()
    }

    override fun onBackPressed() {
        successResetStoreType()
        super.onBackPressed()
    }
}