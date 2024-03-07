package com.ottokonek.ottokasir.presenter

import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BasePresenter
import app.beelabs.com.codebase.base.contract.IView
import app.beelabs.com.codebase.support.rx.RxObserver
import com.ottokonek.ottokasir.model.api.request.CheckRefundRequestModel
import com.ottokonek.ottokasir.model.api.request.RefundRequestModel
import com.ottokonek.ottokasir.model.api.response.CheckRefundResponseModel
import com.ottokonek.ottokasir.model.api.response.RefundResponseModel
import com.ottokonek.ottokasir.model.dao.RefundDao
import com.ottokonek.ottokasir.ui.callback.ViewBaseInterface
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class RefundPresenter(iView: IView) : BasePresenter(), RefundDao.IRefund {

    var refundIView: IView = iView
    val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onRefund(data: RefundRequestModel, activity: BaseActivity) {
        RefundDao(this).onRefund(data, activity).subscribe(object : RxObserver<RefundResponseModel>(refundIView, null) {
            override fun onSubscribe(d: Disposable) {
                super.onSubscribe(d)
                compositeDisposable.add(d)
            }

            override fun onNext(o: Any) {
                super.onNext(o)
                o as RefundResponseModel
                if (o.meta?.code == 200) {
                    (refundIView as SuccessRefundIView).onSuccessRefund(o)
                } else {
                    (refundIView as SuccessRefundIView).onSuccessRefund(o)
                }
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                (refundIView as SuccessRefundIView).handleError(e!!.localizedMessage)
            }
        })
    }

    override fun onCheckRefund(data: CheckRefundRequestModel, activity: BaseActivity) {
        RefundDao(this).onCheckRefund(data, activity).subscribe(object : RxObserver<CheckRefundResponseModel>(refundIView, null) {
            override fun onSubscribe(d: Disposable) {
                super.onSubscribe(d)
                compositeDisposable.add(d)
            }

            override fun onNext(o: Any) {
                super.onNext(o)
                o as CheckRefundResponseModel
                if (o.meta?.code == 200) {
                    (refundIView as CheckRefundIView).onSuccessCheckRefund(o)
                } else {
                    (refundIView as CheckRefundIView).onSuccessCheckRefund(o)
                }
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                (refundIView as CheckRefundIView).handleError(e!!.localizedMessage)
            }
        })
    }


    override fun onDestroy() {
        compositeDisposable.clear()
    }


    interface CheckRefundIView : ViewBaseInterface {

        fun onSuccessCheckRefund(data: CheckRefundResponseModel)
    }

    interface SuccessRefundIView : ViewBaseInterface {

        fun onSuccessRefund(data: RefundResponseModel)
    }
}