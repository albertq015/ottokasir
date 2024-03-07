package com.ottokonek.ottokasir.ui.activity.history

import com.ottokonek.ottokasir.model.api.response.DailyOmzetResponseModel
import com.ottokonek.ottokasir.model.api.response.TransactionHistoryResponse
import com.ottokonek.ottokasir.ui.callback.ViewBaseInterface

interface TransactionIView : ViewBaseInterface {


    fun handleSuccess(data:TransactionHistoryResponse)
    fun saveToRealm(data:TransactionHistoryResponse)
    fun handleSuccessLoadMore(data:DailyOmzetResponseModel)
    fun sessionExpired()
}