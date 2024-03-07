package com.ottokonek.ottokasir.ui.dialog.kasbon

import android.content.Context
import android.os.Bundle
import androidx.annotation.NonNull
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ottokonek.ottokasir.R
import kotlinx.android.synthetic.main.dialog_processing_refund.*

class KasbonWarningDialog (@NonNull context: Context, style: Int, private val callbackFilter: KasbonWarningCallback) : BottomSheetDialog(context, style) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_warning_kasbon)
        setCancelable(true)

        btnOk.setOnClickListener {
            this.dismiss()
            callbackFilter.onOkClearOrderIdsSelected()
        }
    }
}