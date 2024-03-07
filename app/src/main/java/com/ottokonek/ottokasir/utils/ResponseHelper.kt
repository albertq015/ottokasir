package com.ottokonek.ottokasir.utils

import app.beelabs.com.codebase.base.contract.IView
import app.beelabs.com.codebase.base.response.BaseResponse
import com.ottokonek.ottokasir.App.Companion.context
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.presenter.manager.SessionManager.logoutDevice
import com.ottokonek.ottokasir.ui.dialog.loading.CustomProgressDialog

object ResponseHelper {

    fun validateResponse(baseResponse: BaseResponse?, iv: IView, tag: String): Boolean {
        val message: String
        if (baseResponse == null) {
            message = "response from endpoint is empty or null"
            iv.handleError(message)
            LogHelper(tag, message).run()
            return false
        }

        if (baseResponse.baseMeta == null) {
            message = "meta key on response is empty or null"
            iv.handleError(message)
            LogHelper(tag, message).run()
            return false
        }

        if (!baseResponse.baseMeta.isStatus) {
            message = baseResponse.baseMeta.message
            iv.handleError(baseResponse.baseMeta.message)
            LogHelper(tag, message).run()

            // check if token is expired
            if (baseResponse.baseMeta.code == 401) {
                try {
                    CustomProgressDialog.closeDialog()
                    logoutDevice(iv.currentActivity)
                } catch (e: Exception) {
                    LogHelper(tag, "error while do logout device: " + e.message)
                }
            }

            return false
        }

        if (baseResponse.baseMeta.code != 200 && baseResponse.baseMeta.code != 201) {
            message = baseResponse.baseMeta.message
            iv.handleError(message)
            LogHelper(tag, message).run()
            return false
        }

        return true
    }

    fun validateMessageError(message: String):
            String = if (message.contains("Failed to connect to /13.229.100.66:8117") || message.contains("Failed to connect to /34.101.87.199:8993") || message.contains("Unable to resolve host \"ottokonek.ottopay.id\": No address associated with hostname")) {
        context!!.getString(R.string.tidak_ada_koneksi)
    } else {
        message
    }
}