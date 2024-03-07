package com.ottokonek.ottokasir.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable

open class BaseDialogApp(context: Context): Dialog(context) {

    override fun onStart() {
        super.onStart()

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}