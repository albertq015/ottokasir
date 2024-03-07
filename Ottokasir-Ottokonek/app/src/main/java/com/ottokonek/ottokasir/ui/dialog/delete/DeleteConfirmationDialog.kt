package com.ottokonek.ottokasir.ui.dialog.delete

import android.app.Activity
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.view.WindowManager
import com.ottokonek.ottokasir.R
import kotlinx.android.synthetic.main.dialog_confirmation_for_delete.*

class DeleteConfirmationDialog(val activity: Activity, style: Int, val callback: DeleteConfirmationCallback) : BottomSheetDialog(activity, style) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_confirmation_for_delete)
        setCancelable(false)
        val lWindowParams: WindowManager.LayoutParams =  WindowManager.LayoutParams()
        lWindowParams.copyFrom(getWindow()?.getAttributes());
        lWindowParams.width = WindowManager.LayoutParams.MATCH_PARENT
        lWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        this.getWindow()?.setAttributes(lWindowParams)
        buttonBack.setOnClickListener {
            this.dismiss()
        }
        buttonDeleteProduct.setOnClickListener {
            this.dismiss()
            callback.onDelete()
        }
    }
}