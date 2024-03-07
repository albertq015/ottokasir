package com.ottokonek.ottokasir.ui.dialog

import android.app.Activity
import android.os.Bundle
import app.beelabs.com.codebase.base.BaseDialog
import app.beelabs.com.codebase.base.BasePresenter
import app.beelabs.com.codebase.base.contract.IView
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.presenter.AuthPresenter
import kotlinx.android.synthetic.main.dialog_info.*

class InfoDialog(val activity: Activity, style: Int) : BaseDialog(activity, style) {
    var message = activity.resources.getString(R.string.info_logout)
    var handleLogout = true
    var callback: InfoDialog.Callback? = null

    interface Callback {
        fun onCancel()
    }

    constructor(activity: Activity, style: Int, mMessage: String?, mHandleLogout: Boolean) : this(activity, style) {
        message = mMessage.toString()
        handleLogout = mHandleLogout
    }

    constructor(activity: Activity, style: Int, mMessage: String?, mHandleLogout: Boolean, callback: Callback) : this(activity, style) {
        message = mMessage.toString()
        handleLogout = mHandleLogout
        this.callback = callback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setWindowContentDialogLayout(R.layout.dialog_info)
        tvInfoText.text = message
        ivClose.setOnClickListener {
            if (handleLogout) {
                logout()
            } else {
                closeDialog()
            }
        }
    }

    fun logout() {
        val presenter = BasePresenter.getInstance(activity as IView, AuthPresenter::class.java) as AuthPresenter
        this.dismiss()
        presenter.doLogout(activity)
    }

    override fun onBackPressed() {
        closeDialog()
        super.onBackPressed()
    }

    fun closeDialog() {
        this.dismiss()
        callback?.onCancel()
    }
}