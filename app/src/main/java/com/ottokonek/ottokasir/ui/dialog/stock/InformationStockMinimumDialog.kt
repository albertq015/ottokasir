package com.ottokonek.ottokasir.ui.dialog.stock

import android.content.Context
import android.os.Bundle
import androidx.annotation.NonNull
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ottokonek.ottokasir.R
import kotlinx.android.synthetic.main.dialog_information_stock.*

class InformationStockMinimumDialog(@NonNull context: Context, style: Int) : BottomSheetDialog(context, style) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_information_stock)
        setCanceledOnTouchOutside(true)

        tvTittle.text = context.getString(R.string.stok_minimum)
        tvContent.text = context.getString(R.string.content_stok_minimum)

        btnBack.setOnClickListener {
            this.dismiss()
        }

    }

    override fun onBackPressed() {
        this.dismiss()
        super.onBackPressed()
    }
}