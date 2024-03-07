package com.ottokonek.ottokasir.ui.dialog

import android.content.Context
import android.os.Bundle
import app.beelabs.com.codebase.base.BaseDialog
import com.ottokonek.ottokasir.R

class CalendarPickerDialog(context: Context, style: Int) : BaseDialog(context, style) {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.dialog_calendar_picker)
    }
}