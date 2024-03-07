package com.ottokonek.ottokasir.ui.activity.payment

import com.ottokonek.ottokasir.model.api.response.PaymentCashBondResponseModel
import com.ottokonek.ottokasir.ui.callback.ViewBaseInterface

interface PaymentCashBondIView : ViewBaseInterface {

    fun handleProcessing()
    fun handleSuccess(data: PaymentCashBondResponseModel)
}