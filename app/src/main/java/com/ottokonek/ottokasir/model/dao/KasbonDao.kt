package com.ottokonek.ottokasir.model.dao

import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BaseDao
import com.ottokonek.ottokasir.model.api.Api
import com.ottokonek.ottokasir.model.api.request.*
import com.ottokonek.ottokasir.model.api.response.*
import com.ottokonek.ottokasir.presenter.KasbonPresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class KasbonDao(dao: KasbonPresenter) : BaseDao() {


    interface IKasbon {
        fun onKasbonAktif(data: KasbonAktifRequestModel, activity: BaseActivity)
        fun onKasbonLunas(data: KasbonLunasRequestModel, activity: BaseActivity)
        fun onKasbonCustomer(data: KasbonCustomerRequestModel, activity: BaseActivity)
        fun onKasbonCustomerDetail(data: KasbonCustomerDetailRequestModel, activity: BaseActivity)
        fun onKasbonReport(data: KasbonReportRequestModel, activity: BaseActivity)
        fun onKasbonExport(data: KasbonExportRequestModel, activity: BaseActivity)
        fun onKasbonAktifSelected(data: KasbonAktifSelectedRequestModel, activity: BaseActivity)
        fun onKasbonCashPayment(data: KasbonCashPaymentRequestModel, activity: BaseActivity)
        fun onKasbonQrPaymentGenerate(data: KasbonQrPaymentRequestModel, activity: BaseActivity)
        fun onKasbonQrPaymentStatus(data: KasbonQrPaymentStatusRequestModel, activity: BaseActivity)
        fun onKasbonQrPaymentStatusAuto(data: KasbonQrPaymentStatusRequestModel, count: Int, activity: BaseActivity)
    }

    fun onKasbonAktif(data: KasbonAktifRequestModel, activity: BaseActivity): Observable<KasbonAktifResponseModel> {
        return Api.onKasbonAktif(data, activity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }


    fun onKasbonLunas(data: KasbonLunasRequestModel, activity: BaseActivity): Observable<KasbonLunasResponseModel> {
        return Api.onKasbonLunas(data, activity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }


    fun onKasbonCustomer(data: KasbonCustomerRequestModel, activity: BaseActivity): Observable<KasbonCustomerResponseModel> {
        return Api.onKasbonCustomer(data, activity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }


    fun onKasbonCustomerDetail(data: KasbonCustomerDetailRequestModel, activity: BaseActivity): Observable<KasbonAktifSelectedResponseModel> {
        return Api.onKasbonCustomerDetail(data, activity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun onKasbonReport(data: KasbonReportRequestModel, activity: BaseActivity): Observable<KasbonReportResponseModel> {
        return Api.onKasbonReport(data, activity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun onKasbonExport(data: KasbonExportRequestModel, activity: BaseActivity): Observable<KasbonExportResponseModel> {
        return Api.onKasbonExport(data, activity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun onKasbonAktifSelected(data: KasbonAktifSelectedRequestModel, activity: BaseActivity): Observable<KasbonAktifSelectedResponseModel> {
        return Api.onKasbonAktifSelected(data, activity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }


    fun onKasbonCashPayment(data: KasbonCashPaymentRequestModel, activity: BaseActivity): Observable<KasbonCashPaymentResponseModel> {
        return Api.onKasbonCashPayment(data, activity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }


    fun onKasbonQrPaymentGenerate(data: KasbonQrPaymentRequestModel, activity: BaseActivity): Observable<KasbonQrPaymentResponseModel> {
        return Api.onKasbonQrPaymentGenerate(data, activity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }


    fun onKasbonQrPaymentStatus(data: KasbonQrPaymentStatusRequestModel, activity: BaseActivity): Observable<KasbonQrPaymentStatusResponseModel> {
        return Api.onKasbonQrPaymentStatus(data, activity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

}