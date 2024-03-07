package com.ottokonek.ottokasir.presenter

import android.app.Activity
import android.os.Handler
import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BasePresenter
import app.beelabs.com.codebase.base.contract.IView
import app.beelabs.com.codebase.base.response.BaseResponse
import app.beelabs.com.codebase.support.rx.RxObserver
import com.ottokonek.ottokasir.App
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.base.ObserverApi
import com.ottokonek.ottokasir.dao.transaction.TransactionManager
import com.ottokonek.ottokasir.model.api.request.*
import com.ottokonek.ottokasir.model.api.response.*
import com.ottokonek.ottokasir.model.dao.OrderDao
import com.ottokonek.ottokasir.model.miscModel.ItemModel
import com.ottokonek.ottokasir.ui.activity.payment.PaymentCashIView
import com.ottokonek.ottokasir.ui.activity.payment.PaymentQrIView
import com.ottokonek.ottokasir.ui.activity.history.TransactionIView
import com.ottokonek.ottokasir.ui.activity.payment.PaymentCashBondIView
import com.ottokonek.ottokasir.ui.dialog.InfoDialog
import com.ottokonek.ottokasir.ui.fragment.OmzetIView
import com.ottokonek.ottokasir.utils.LogHelper
import com.ottokonek.ottokasir.utils.ResponseHelper

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class OrderPresenter(val iView: IView) : BasePresenter(), OrderDao.IOrderDao {

    private val TAG = OrderPresenter::class.java.simpleName


    private var orderIView: IView = iView
    private lateinit var dialogInfo: InfoDialog
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    companion object {
        fun clearTransactionRealm() {
            TransactionManager.delTransaction()
        }
    }

    override fun getAvailableOmzet(activity: BaseActivity) {

        OrderDao(this).onAvailableOmzetDAO(activity).subscribe(object : RxObserver<AvailableOmzetResponseModel>(
                orderIView, null) {

            override fun onSubscribe(d: Disposable) {
                super.onSubscribe(d)
                compositeDisposable.add(d)
            }

            override fun onNext(o: Any) {
                if ((o as AvailableOmzetResponseModel).meta?.code == 200) {
                    (orderIView as OmzetIView).setAvailableOmzet(o as AvailableOmzetResponseModel)
                } else if (o.meta?.code == 498) {
                    (orderIView as OmzetIView).hideLoading()
                    dialogInfo = InfoDialog(activity, R.style.CustomDialog)
                    dialogInfo.show()
                } else {
                    (orderIView as OmzetIView).handleError(o.meta?.message!!)
                }
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                (orderIView as OmzetIView).hideLoading()
            }
        })
    }

    override fun getTransactionHistory(activity: Activity, model: HistoryRequestModel) {
        OrderDao(this).onTransactionHistory(model).subscribe(object : RxObserver<TransactionHistoryResponse>(
                orderIView, null) {


            override fun onSubscribe(d: Disposable) {
                super.onSubscribe(d)
                compositeDisposable.add(d)
            }

            override fun onNext(o: Any) {
                if ((o as TransactionHistoryResponse).baseMeta?.code == 200) {
                    (orderIView as TransactionIView).handleSuccess(o as TransactionHistoryResponse)
                    (orderIView as TransactionIView).hideLoading()
                } else if (o.baseMeta.code == 401) {
                    (orderIView as TransactionIView).sessionExpired()
                    (orderIView as TransactionIView).hideLoading()
                } else {
                    (orderIView as TransactionIView).hideLoading()
                }
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                (orderIView as TransactionIView).hideLoading()
                //(orderIView as TransactionIView).onConnectionFailed(e!!.localizedMessage)
            }

            /*override fun onNext(o: Any) {
                if ((o as TransactionHistoryResponse).meta?.code == 200) {
                    //(orderIView as TransactionIView).hideLoading()
                    (orderIView as TransactionIView).handleSuccess(o)
                } else if (o.meta?.code == 498) {
                    (orderIView as TransactionIView).hideLoading()
                    dialogInfo = InfoDialog(activity, R.style.CustomDialog)
                    dialogInfo.show()
                    //(orderIView as TransactionIView).handleError(o.meta?.message!!)
                    //(orderIView as TransactionIView).logout()
                } else {
                    (orderIView as TransactionIView).handleError(o.meta?.message!!)
                }
            }

            override fun onError(e: Throwable) {
                (orderIView as TransactionIView).onConnectionFailed(e!!.localizedMessage)

            }*/

        })
    }

    override fun onQrPaymentGenerate(model: QrPaymentGenerateRequestModel, activity: BaseActivity) {
        OrderDao(this).onQrPaymentGenerate(model, activity).subscribe(object : ObserverApi(activity) {

            override fun onSubscribe(d: Disposable) {
                //super.onSubscribe(d)
                compositeDisposable.add(d)
            }


            override fun onNext(value: BaseResponse) {
                if (ResponseHelper.validateResponse(value, orderIView, TAG))
                    (orderIView as PaymentQrIView).onSuccessGenerateQr(value as PaymentQrResponseModel)
            }


            override fun onError(e: Throwable) {
                super.onError(e)
                (orderIView as PaymentQrIView).onBadConnectionGenerateQr()
                //LogHelper(TAG, e.localizedMessage).run() onConnectionFailed(e!!.localizedMessage)
                //orderIView.handleError(ResponseHelper.validateMessageError(e.message?: App.context!!.getString(R.string.error_global)))
            }
        })
    }

    override fun onQrPaymentCheckStatus(model: QrPaymentCheckStatRequestModel, activity: BaseActivity) {
        OrderDao(this).onQrPaymentCheckStatus(model, activity).subscribe(object : ObserverApi(activity) {

            override fun onNext(value: BaseResponse) {
                if (ResponseHelper.validateResponse(value, orderIView, TAG))
                    (orderIView as PaymentQrIView).onSuccessCheckStatusQr(value as CheckStatusQrResponseModel)
            }


            override fun onError(e: Throwable) {
                LogHelper(TAG, e.localizedMessage).run()
                orderIView.handleError(ResponseHelper.validateMessageError(e.message
                        ?: App.context!!.getString(R.string.error_global)))
            }
        })
    }

    override fun onQrPaymentCheckStatusAuto(model: QrPaymentCheckStatRequestModel, count: Int, activity: BaseActivity) {

        val currentCount = count + 1
        OrderDao(this).onQrPaymentCheckStatus(model, activity).subscribe(object : ObserverApi(activity) {


            override fun onSubscribe(d: Disposable) {
                super.onSubscribe(d)
                (orderIView as PaymentQrIView).handleCheckProcessing()
                compositeDisposable.add(d)
            }

            override fun onNext(value: BaseResponse) {

                val result = value as CheckStatusQrResponseModel

                if (result.data?.status == "Pending" && count < 9) {
                    val handler = Handler()
                    handler.postDelayed({
                        onQrPaymentCheckStatusAuto(model, currentCount, activity)
                    }, 2000)
                } else if (result.data?.status == "Pending" && count >= 9) {
                    (orderIView as PaymentQrIView).handleCheckComplete()
                } else {
                    (orderIView as PaymentQrIView).onSuccessCheckStatusQr(value as CheckStatusQrResponseModel)
                }

                /*if (value.baseMeta.code != 200 && count < 9) {
                    val handler = Handler()
                    handler.postDelayed({
                        onQrPaymentCheckStatusAuto(model, currentCount, activity)
                    }, 2000)
                } else if (value.baseMeta.code == 200) {
                    (orderIView as PaymentQrIView).onSuccessCheckStatusQr(value as CheckStatusQrResponseModel)
                } else {
                    (orderIView as PaymentQrIView).handleCheckComplete()
                }*/
            }


            override fun onError(e: Throwable) {
                (orderIView as PaymentQrIView).onBadConnectionCheckStatus()
                /*LogHelper(TAG, e.localizedMessage).run()
                orderIView.handleError(ResponseHelper.validateMessageError(e.message
                        ?: App.context!!.getString(R.string.error_global)))*/
            }
        })

    }


    override fun onPaymentCash(activity: Activity, model: PaymentCashRequestModel) {
        OrderDao(this).onPaymentCash(model).subscribe(object : RxObserver<PaymentCashResponseModel>(
                orderIView, null) {

            override fun onSubscribe(d: Disposable) {
                super.onSubscribe(d)
                compositeDisposable.add(d)
            }

            override fun onNext(o: Any) {
                if ((o as PaymentCashResponseModel).meta?.code == 200) {
                    (orderIView as PaymentCashIView).handleSuccess(o as PaymentCashResponseModel)

                } else if (o.meta?.code == 498) {
                    (orderIView as PaymentCashIView).hideLoading()
                    dialogInfo = InfoDialog(activity, R.style.CustomDialog)
                    dialogInfo.show()
                } else {
                    (orderIView as PaymentCashIView).handleError(o.meta?.message!!)
                }
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                (orderIView as PaymentCashIView).onConnectionFailed(e!!.localizedMessage)
            }
        })
    }

    override fun onPaymentCashBond(activity: Activity, model: PaymentCashBondRequestModel) {
        OrderDao(this).onPaymentCashBond(model).subscribe(object : RxObserver<PaymentCashBondResponseModel>(
                orderIView, null) {

            override fun onSubscribe(d: Disposable) {
                super.onSubscribe(d)
                compositeDisposable.add(d)
            }

            override fun onNext(o: Any) {
                if ((o as PaymentCashBondResponseModel).meta?.code == 200) {
                    (orderIView as PaymentCashBondIView).handleSuccess(o)

                } else if (o.meta?.code == 498) {
                    (orderIView as PaymentCashBondIView).hideLoading()
                    dialogInfo = InfoDialog(activity, R.style.CustomDialog)
                    dialogInfo.show()
                } else {
                    (orderIView as PaymentCashBondIView).handleError(o.meta?.message!!)
                }
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                (orderIView as PaymentCashBondIView).onConnectionFailed(e!!.localizedMessage)
            }
        })
    }


    override fun saveToRealm(data: TransactionHistoryResponse) {
        if (data != null) {
            TransactionManager.putTransaction(data)
        }
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
    }

    override fun onClear() {
        compositeDisposable.clear()
    }

    fun createOrderCash(paidNominal: String, totalAmount: String, paymentType: String, customerId: Int): PaymentCashRequestModel {
        return TransactionManager.createOrderCash(paidNominal, totalAmount, paymentType, customerId)
    }

    fun createOrderCashBond(totalAmount: String, paymentType: String, customerId: Int): PaymentCashBondRequestModel {
        return TransactionManager.createOrderCashBond(totalAmount, paymentType, customerId)
    }

    fun getTotalPrice(): Double {
        return TransactionManager.getTotalPrice()
    }

    fun getProductInOrder(): ArrayList<ItemModel>? {
        return TransactionManager.getProductsInOrderForRequest()
    }


}