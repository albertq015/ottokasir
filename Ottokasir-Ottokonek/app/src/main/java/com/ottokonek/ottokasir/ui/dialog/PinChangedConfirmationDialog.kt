package com.ottokonek.ottokasir.ui.dialog

import android.content.Context
import android.os.Bundle
import app.beelabs.com.codebase.base.BaseDialog
import com.ottokonek.ottokasir.R
import kotlinx.android.synthetic.main.dialog_pin_changed_confirmation.*

class PinChangedConfirmationDialog(context: Context, style: Int) : BaseDialog(context, style) {

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.dialog_pin_changed_confirmation)

        doneBtn.setOnClickListener {
            dismiss()
        }
    }
}