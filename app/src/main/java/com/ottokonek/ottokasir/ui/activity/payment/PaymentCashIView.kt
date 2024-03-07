package com.ottokonek.ottokasir.ui.activity.payment

import com.ottokonek.ottokasir.model.api.response.PaymentCashResponseModel
import com.ottokonek.ottokasir.ui.callback.ViewBaseInterface

interface PaymentCashIView : ViewBaseInterface {

    fun handleSuccess(data:PaymentCashResponseModel)

}