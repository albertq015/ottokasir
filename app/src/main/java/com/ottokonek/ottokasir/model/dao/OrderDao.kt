package com.ottokonek.ottokasir.model.dao

import android.app.Activity
import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BaseDao
import com.ottokonek.ottokasir.model.api.Api
import com.ottokonek.ottokasir.model.api.request.*
import com.ottokonek.ottokasir.model.api.response.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class OrderDao(dao: IOrderDao) : BaseDao() {

    interface IOrderDao {
        fun getAvailableOmzet(activity: BaseActivity)

        fun getTransactionHistory(activity: Activity, model: HistoryRequestModel)

        fun onQrPaymentGenerate(model: QrPaymentGenerateRequestModel, activity: BaseActivity)

        fun onQrPaymentCheckStatus(model: QrPaymentCheckStatRequestModel, activity: BaseActivity)

        fun onQrPaymentCheckStatusAuto(model: QrPaymentCheckStatRequestModel, count: Int, activity: BaseActivity)

        fun onPaymentCash(activity: Activity, model: PaymentCashRequestModel)

        fun onPaymentCashBond(activity: Activity, model: PaymentCashBondRequestModel)

        fun saveToRealm(data: TransactionHistoryResponse)
        fun onDestroy()
        fun onClear()
    }

    fun onAvailableOmzetDAO(activity: BaseActivity): Observable<AvailableOmzetResponseModel> {
        return Api.onAvailableOmzet(activity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun onTransactionHistory(model: HistoryRequestModel): Observable<TransactionHistoryResponse> {
        return Api.onTransactionHistory(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun onPaymentCash(model: PaymentCashRequestModel): Observable<PaymentCashResponseModel> {
        return Api.onPaymentCash(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun onPaymentCashBond(model: PaymentCashBondRequestModel): Observable<PaymentCashBondResponseModel> {
        return Api.onPaymentCashBond(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun onQrPaymentGenerate(model: QrPaymentGenerateRequestModel, activity: BaseActivity): Observable<PaymentQrResponseModel> {
        return Api.onQrPaymentGenerate(model, activity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun onQrPaymentCheckStatus(model: QrPaymentCheckStatRequestModel, activity: BaseActivity): Observable<CheckStatusQrResponseModel> {
        return Api.onQrPaymentCheckStatus(model, activity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}