package com.ottokonek.ottokasir.ui.dialog

import android.content.Context
import android.os.Bundle
import androidx.annotation.NonNull
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ottokonek.ottokasir.R
import kotlinx.android.synthetic.main.dialog_processing_refund.*

class AddProductDialog(@NonNull context: Context, style: Int) : BottomSheetDialog(context, style) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_add_product)
        setCancelable(false)

        btnOk.setOnClickListener {
            this.dismiss()
        }
    }
}