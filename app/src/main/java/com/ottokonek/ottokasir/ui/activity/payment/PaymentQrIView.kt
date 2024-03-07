package com.ottokonek.ottokasir.ui.activity.payment

import com.ottokonek.ottokasir.model.api.response.CheckStatusQrResponseModel
import com.ottokonek.ottokasir.model.api.response.PaymentQrResponseModel
import com.ottokonek.ottokasir.ui.callback.ViewBaseInterface

interface PaymentQrIView : ViewBaseInterface {

    //fun handleProcessing()
    fun onSuccessGenerateQr(data: PaymentQrResponseModel)
    fun onSuccessCheckStatusQr(data: CheckStatusQrResponseModel)
    fun handleCheckProcessing()
    fun handleCheckComplete()
    fun onBadConnectionGenerateQr()
    fun onBadConnectionCheckStatus()

}