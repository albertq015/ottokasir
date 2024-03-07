package com.ottokonek.ottokasir.ui.dialog.report

import android.content.Context
import android.os.Bundle
import androidx.annotation.NonNull
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ottokonek.ottokasir.R
import kotlinx.android.synthetic.main.dialog_export_report.*

class ExportReportToDialog(@NonNull context: Context, style: Int, private val callbackExport: ExportReportToCallback) : BottomSheetDialog(context, style) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_export_report)
        setCancelable(false)

        actionExportReport()
    }

    private fun actionExportReport() {
        iv_close.setOnClickListener {
            this.dismiss()
        }

        ly_device.setOnClickListener {
            this.dismiss()
            callbackExport.ExportToDevice()
        }

        ly_email.setOnClickListener {
            this.dismiss()
            callbackExport.ExportToEmail()
        }
    }


    override fun onBackPressed() {
        dismiss()
    }
}