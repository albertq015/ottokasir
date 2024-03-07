package com.ottokonek.ottokasir.presenter

import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.contract.IView
import app.beelabs.com.codebase.base.response.BaseResponse
import com.ottokonek.ottokasir.App
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.base.BasePresenterApp
import com.ottokonek.ottokasir.base.ObserverApi
import com.ottokonek.ottokasir.dao.transaction.TransactionManager
import com.ottokonek.ottokasir.model.api.request.*
import com.ottokonek.ottokasir.model.api.response.*
import com.ottokonek.ottokasir.model.dao.CustomerDao
import com.ottokonek.ottokasir.model.pojo.CustomerModel
import com.ottokonek.ottokasir.ui.callback.ViewBaseInterface
import com.ottokonek.ottokasir.utils.LogHelper
import com.ottokonek.ottokasir.utils.ResponseHelper
import io.reactivex.disposables.Disposable

class CustomerPresenter(iv: IView) : BasePresenterApp(iv), CustomerDao.ICustomer {


    private val TAG = CustomerPresenter::class.java.simpleName

    override fun onCustomerList(data: CustomerListRequestModel, activity: BaseActivity) {

        CustomerDao(this).onCustomerList(data, activity).subscribe(object : ObserverApi(activity) {

            override fun onSubscribe(d: Disposable) {
                super.onSubscribe(d)
            }

            override fun onNext(value: BaseResponse) {
                if (ResponseHelper.validateResponse(value, iv, TAG)) {
                    val result = value as CustomerListResponseModel

                    val items = mutableListOf<CustomerModel>()
                    result.data?.merchant_customers?.let {
                        for (item in it)
                            items.add(CustomerModel(item.id, item.name, item.phone))
                    }

                    (iv as ICustomerListView).onSuccessCustomerList(items, result)
                }
            }

            override fun onError(e: Throwable) {
                LogHelper(TAG, e.localizedMessage).run()
                iv.handleError(ResponseHelper.validateMessageError(e.message
                        ?: App.context!!.getString(R.string.error_global)))
            }
        })
    }

    override fun onCustomerCreate(data: CustomerCreateRequestModel, activity: BaseActivity) {

        CustomerDao(this).onCustomerCreate(data, activity).subscribe(object : ObserverApi(activity) {

            override fun onNext(value: BaseResponse) {
                if (ResponseHelper.validateResponse(value, iv, TAG))
                    (iv as ICustomerCreateView).onSuccessCustomerCreate(
                            value as CustomerCreateResponseModel
                    )
            }

            override fun onError(e: Throwable) {
                LogHelper(TAG, e.localizedMessage).run()
                iv.handleError(ResponseHelper.validateMessageError(e.message
                        ?: App.context!!.getString(R.string.error_global)))
            }
        })
    }

    override fun onCustomerDetail(data: CustomerDetailRequestModel, activity: BaseActivity) {

        CustomerDao(this).onCustomerDetail(data, activity).subscribe(object : ObserverApi(activity) {

            override fun onNext(value: BaseResponse) {
                if (ResponseHelper.validateResponse(value, iv, TAG))
                    (iv as ICustomerDetailView).onSuccessCustomerDetail(
                            value as CustomerDetailResponseModel
                    )
            }

            override fun onError(e: Throwable) {
                LogHelper(TAG, e.localizedMessage).run()
                iv.handleError(ResponseHelper.validateMessageError(e.message
                        ?: App.context!!.getString(R.string.error_global)))
            }
        })
    }

    override fun onCustomerDelete(data: CustomerDeleteRequestModel, activity: BaseActivity) {

        CustomerDao(this).onCustomerDelete(data, activity).subscribe(object : ObserverApi(activity) {

            override fun onNext(value: BaseResponse) {
                if (ResponseHelper.validateResponse(value, iv, TAG))
                    (iv as ICustomerDetailView).onSuccessCustomerDelete(value)
            }

            override fun onError(e: Throwable) {
                LogHelper(TAG, e.localizedMessage).run()
                iv.handleError(ResponseHelper.validateMessageError(e.message
                        ?: App.context!!.getString(R.string.error_global)))
            }
        })
    }

    override fun onCustomerUpdate(data: CustomerUpdateRequestModel, activity: BaseActivity) {

        CustomerDao(this).onCustomerUpdate(data, activity).subscribe(object : ObserverApi(activity) {

            override fun onNext(value: BaseResponse) {
                if (ResponseHelper.validateResponse(value, iv, TAG))
                    (iv as ICustomerUpdateView).onSuccessCustomerUpdate(value as CustomerUpdateResponseModel)
            }

            override fun onError(e: Throwable) {
                LogHelper(TAG, e.localizedMessage).run()
                iv.handleError(ResponseHelper.validateMessageError(e.message
                        ?: App.context!!.getString(R.string.error_global)))
            }
        })
    }

    override fun onCustomerHistory(data: CustomerHistoryRequestModel, activity: BaseActivity) {

        CustomerDao(this).onCustomerHistory(data, activity).subscribe(object : ObserverApi(activity) {

            override fun onNext(value: BaseResponse) {
                if (ResponseHelper.validateResponse(value, iv, TAG))
                    (iv as ICustomerHistoryView).onSuccessHistoryTransaction(value as TransactionHistoryResponse)
            }

            override fun onError(e: Throwable) {
                LogHelper(TAG, e.localizedMessage).run()
                iv.handleError(ResponseHelper.validateMessageError(e.message
                        ?: App.context!!.getString(R.string.error_global)))
            }
        })
    }

    override fun onCustomerKasbon(data: CustomerKasbonRequestModel, activity: BaseActivity) {

        CustomerDao(this).onCustomerKasbon(data, activity).subscribe(object : ObserverApi(activity) {

            override fun onNext(value: BaseResponse) {
                if (ResponseHelper.validateResponse(value, iv, TAG))
                    (iv as ICustomerKasbonView).onSuccessCustomerKasbon(value as CustomerKasbonResponseModel)
            }

            override fun onError(e: Throwable) {
                LogHelper(TAG, e.localizedMessage).run()
                iv.handleError(ResponseHelper.validateMessageError(e.message
                        ?: App.context!!.getString(R.string.error_global)))
            }
        })
    }

    override fun saveToRealm(data: TransactionHistoryResponse) {
        TransactionManager.putTransaction(data)
    }


    /**
     * Interface
     * */
    interface ICustomerCreateView : ViewBaseInterface {
        fun onSuccessCustomerCreate(data: CustomerCreateResponseModel)
    }

    interface ICustomerDetailView : ViewBaseInterface {
        fun onSuccessCustomerDetail(data: CustomerDetailResponseModel)
        fun onSuccessCustomerDelete(data: BaseResponse)
    }

    interface ICustomerHistoryView : ViewBaseInterface {
        fun onSuccessHistoryTransaction(result: TransactionHistoryResponse)
        fun saveToRealm(result: TransactionHistoryResponse)
    }

    interface ICustomerKasbonView : ViewBaseInterface {
        fun onSuccessCustomerKasbon(result: CustomerKasbonResponseModel)
    }

    interface ICustomerListView : ViewBaseInterface {
        fun onSuccessCustomerList(result: MutableList<CustomerModel>, resultOri: CustomerListResponseModel)
    }

    interface ICustomerUpdateView : ViewBaseInterface {
        fun onSuccessCustomerUpdate(data: CustomerUpdateResponseModel)
    }

}