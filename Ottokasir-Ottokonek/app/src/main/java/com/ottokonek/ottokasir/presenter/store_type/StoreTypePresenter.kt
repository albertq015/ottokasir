package com.ottokonek.ottokasir.presenter.store_type

import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.contract.IView
import app.beelabs.com.codebase.base.response.BaseResponse
import com.ottokonek.ottokasir.App.Companion.context
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.base.BasePresenterApp
import com.ottokonek.ottokasir.base.ObserverApi
import com.ottokonek.ottokasir.model.api.request.StoreTypeListRequestModel
import com.ottokonek.ottokasir.model.api.request.StoreTypeProductAddRequestModel
import com.ottokonek.ottokasir.model.api.request.StoreTypeProductRequestModel
import com.ottokonek.ottokasir.model.api.response.StoreTypeListResponseModel
import com.ottokonek.ottokasir.model.api.response.StoreTypeProductAddResponseModel
import com.ottokonek.ottokasir.model.api.response.StoreTypeProductResponseModel
import com.ottokonek.ottokasir.model.dao.StoreTypeDao
import com.ottokonek.ottokasir.ui.activity.store_type.StoreTypeIView
import com.ottokonek.ottokasir.utils.LogHelper
import com.ottokonek.ottokasir.utils.ResponseHelper

class StoreTypePresenter(iv: IView) : BasePresenterApp(iv), StoreTypeDao.IStoreType {

    private val TAG = StoreTypePresenter::class.java.simpleName

    override fun onGetStoreTypeList(data: StoreTypeListRequestModel, activity: BaseActivity) {

        StoreTypeDao(this)
                .onGetStoreTypeList(data, activity)
                .subscribe(object : ObserverApi(context) {

                    override fun onNext(value: BaseResponse) {
                        if (ResponseHelper.validateResponse(value, iv, TAG))
                            (iv as StoreTypeIView).onSuccessGetStoreTypeList(
                                    value as StoreTypeListResponseModel
                            )
                    }

                    override fun onError(e: Throwable) {
                        LogHelper(TAG, e.localizedMessage).run()
                        iv.handleError(
                                ResponseHelper.validateMessageError(
                                        e.message ?: context!!.getString(
                                                R.string.error_global
                                        )
                                )
                        )
                    }
                })
    }


    override fun onGetStoreTypeProduct(data: StoreTypeProductRequestModel, activity: BaseActivity) {

        StoreTypeDao(this)
                .onGetStoreTypeProduct(data, activity)
                .subscribe(object : ObserverApi(context) {

                    override fun onNext(value: BaseResponse) {
                        if (ResponseHelper.validateResponse(value, iv, TAG))
                            (iv as StoreTypeIView).onSuccessGetStoreTypeProduct(
                                    value as StoreTypeProductResponseModel
                            )
                    }

                    override fun onError(e: Throwable) {
                        LogHelper(TAG, e.localizedMessage).run()
                        iv.handleError(
                                ResponseHelper.validateMessageError(
                                        e.message ?: context!!.getString(
                                                R.string.error_global
                                        )
                                )
                        )
                    }
                })
    }


    override fun onGetsToreTypeProductAdd(data: StoreTypeProductAddRequestModel, activity: BaseActivity) {


        StoreTypeDao(this)
                .onGetStoreTypeProductAdd(data, activity)
                .subscribe(object : ObserverApi(context) {

                    override fun onNext(value: BaseResponse) {
                        if (ResponseHelper.validateResponse(value, iv, TAG))
                            (iv as StoreTypeIView).onSuccessGetStoreTypeProductAdd(
                                    value as StoreTypeProductAddResponseModel
                            )
                    }

                    override fun onError(e: Throwable) {
                        LogHelper(TAG, e.localizedMessage).run()
                        iv.handleError(
                                ResponseHelper.validateMessageError(
                                        e.message ?: context!!.getString(
                                                R.string.error_global
                                        )
                                )
                        )
                    }
                })
    }
}