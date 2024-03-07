package com.ottokonek.ottokasir.ui.dialog.force_update

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.ui.dialog.BaseDialogApp
import kotlinx.android.synthetic.main.dialog_force_update.*

class ForceUpdateDialog(context: Context, val callback: ActionDialog) : BaseDialogApp(context) {

    companion object {

        fun showDialog(context: Context?, mItems: ActionDialog) {
            context?.let {
                ForceUpdateDialog(it, mItems).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_force_update)
        window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        setCancelable(false)

        btnForceUpdate.setOnClickListener {
            this.dismiss()
            callback.onForceUpdate()
        }

    }

    interface ActionDialog {
        fun onForceUpdate()
    }

    /*override fun onBackPressed() {
        this.dismiss()
        super.onBackPressed()
    }*/
}