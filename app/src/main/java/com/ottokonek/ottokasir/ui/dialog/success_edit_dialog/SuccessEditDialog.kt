package com.ottokonek.ottokasir.ui.dialog.success_edit_dialog

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ottokonek.ottokasir.R
import kotlinx.android.synthetic.main.dialog_success_edit.*

class SuccessEditDialog(val activity: Activity, style: Int) : BottomSheetDialog(activity, style) {
    lateinit var message: String
    var isAddProduct = true
    lateinit var callback: SuccessDialogCallback
    lateinit var root: ViewGroup


    constructor(activity: Activity, style: Int, mMessage: String, isAddProduct: Boolean, root: ViewGroup, callback: SuccessDialogCallback) : this(activity, style) {
        message = mMessage
        this.isAddProduct = isAddProduct
        this.callback = callback
        this.root = root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(LayoutInflater.from(activity).inflate(R.layout.dialog_success_edit, null))
        textViewMessage.text = message
        setCancelable(false)
        val lWindowParams: WindowManager.LayoutParams = WindowManager.LayoutParams()
        lWindowParams.copyFrom(getWindow()?.getAttributes());
        lWindowParams.width = WindowManager.LayoutParams.MATCH_PARENT
        lWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        this.getWindow()?.setAttributes(lWindowParams)
        if (isAddProduct) {
            buttonToTransaction.visibility = View.VISIBLE
            buttonAddProduct.visibility = View.VISIBLE
            buttonAddProduct.setOnClickListener {
                this.dismiss()
                callback.onAddMore()
            }
            buttonToTransaction.setOnClickListener {
                this.dismiss()
                callback.onBackToTransaction()
            }
        } else {
            buttonBack.visibility = View.VISIBLE
            buttonBack.setOnClickListener {
                this.dismiss()
                callback.onSuccessUpdate()
            }
        }
    }
}