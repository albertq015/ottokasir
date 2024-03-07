package com.ottokonek.ottokasir.model.dao

import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BaseDao
import app.beelabs.com.codebase.base.response.BaseResponse
import com.ottokonek.ottokasir.model.api.Api
import com.ottokonek.ottokasir.model.api.request.*
import com.ottokonek.ottokasir.model.api.response.*
import com.ottokonek.ottokasir.presenter.CustomerPresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CustomerDao(dao: CustomerPresenter) : BaseDao() {

    interface ICustomer {
        fun onCustomerList(data: CustomerListRequestModel, activity: BaseActivity)
        fun onCustomerCreate(data: CustomerCreateRequestModel, activity: BaseActivity)
        fun onCustomerDetail(data: CustomerDetailRequestModel, activity: BaseActivity)
        fun onCustomerDelete(data: CustomerDeleteRequestModel, activity: BaseActivity)
        fun onCustomerUpdate(data: CustomerUpdateRequestModel, activity: BaseActivity)
        fun onCustomerHistory(data: CustomerHistoryRequestModel, activity: BaseActivity)
        fun onCustomerKasbon(data: CustomerKasbonRequestModel, activity: BaseActivity)
        fun saveToRealm(data: TransactionHistoryResponse)
    }

    fun onCustomerList(data: CustomerListRequestModel, activity: BaseActivity): Observable<CustomerListResponseModel> {
        return Api.onCustomerList(data, activity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun onCustomerCreate(data: CustomerCreateRequestModel, activity: BaseActivity): Observable<CustomerCreateResponseModel> {
        return Api.onCustomerCreate(data, activity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun onCustomerDetail(data: CustomerDetailRequestModel, activity: BaseActivity): Observable<CustomerDetailResponseModel> {
        return Api.onCustomerDetail(data, activity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun onCustomerDelete(data: CustomerDeleteRequestModel, activity: BaseActivity): Observable<BaseResponse> {
        return Api.onCustomerDelete(data, activity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun onCustomerUpdate(data: CustomerUpdateRequestModel, activity: BaseActivity): Observable<CustomerUpdateResponseModel> {
        return Api.onCustomerUpdate(data, activity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun onCustomerHistory(data: CustomerHistoryRequestModel, activity: BaseActivity): Observable<TransactionHistoryResponse> {
        return Api.onCustomerHistory(data, activity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun onCustomerKasbon(data: CustomerKasbonRequestModel, activity: BaseActivity): Observable<CustomerKasbonResponseModel> {
        return Api.onCustomerKasbon(data, activity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

}