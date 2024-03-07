package com.ottokonek.ottokasir.model.dao

import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BaseDao
import com.ottokonek.ottokasir.model.api.Api
import com.ottokonek.ottokasir.model.api.request.CheckRefundRequestModel
import com.ottokonek.ottokasir.model.api.request.RefundRequestModel
import com.ottokonek.ottokasir.model.api.response.CheckRefundResponseModel
import com.ottokonek.ottokasir.model.api.response.RefundResponseModel
import com.ottokonek.ottokasir.presenter.RefundPresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RefundDao(dao: RefundPresenter) : BaseDao() {

    interface IRefund {
        fun onRefund(data: RefundRequestModel, activity: BaseActivity)
        fun onCheckRefund(data: CheckRefundRequestModel, activity: BaseActivity)
        fun onDestroy()
    }

    fun onRefund(data: RefundRequestModel, activity: BaseActivity): Observable<RefundResponseModel> {
        return Api.onRefund(data, activity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun onCheckRefund(data: CheckRefundRequestModel, activity: BaseActivity): Observable<CheckRefundResponseModel> {
        return Api.onCheckRefund(data, activity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

}