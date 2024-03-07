package com.ottokonek.ottokasir.presenter

import android.os.Handler
import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.contract.IView
import app.beelabs.com.codebase.base.response.BaseResponse
import com.ottokonek.ottokasir.App
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.base.BasePresenterApp
import com.ottokonek.ottokasir.base.ObserverApi
import com.ottokonek.ottokasir.model.api.request.*
import com.ottokonek.ottokasir.model.api.response.*
import com.ottokonek.ottokasir.model.dao.KasbonDao
import com.ottokonek.ottokasir.ui.callback.ViewBaseInterface
import com.ottokonek.ottokasir.utils.LogHelper
import com.ottokonek.ottokasir.utils.ResponseHelper

class KasbonPresenter(iv: IView) : BasePresenterApp(iv), KasbonDao.IKasbon {

    private val TAG = CustomerPresenter::class.java.simpleName

    override fun onKasbonAktif(data: KasbonAktifRequestModel, activity: BaseActivity) {

        KasbonDao(this).onKasbonAktif(data, activity).subscribe(object : ObserverApi(activity) {

            override fun onNext(value: BaseResponse) {
                if (ResponseHelper.validateResponse(value, iv, TAG))
                    (iv as IKasbonAktifView).onSuccessKasbonAktif(value as KasbonAktifResponseModel)
            }


            override fun onError(e: Throwable) {
                LogHelper(TAG, e.localizedMessage).run()
                iv.handleError(ResponseHelper.validateMessageError(e.message
                        ?: App.context!!.getString(R.string.error_global)))
            }
        })
    }


    override fun onKasbonLunas(data: KasbonLunasRequestModel, activity: BaseActivity) {

        KasbonDao(this).onKasbonLunas(data, activity).subscribe(object : ObserverApi(activity) {

            override fun onNext(value: BaseResponse) {
                if (ResponseHelper.validateResponse(value, iv, TAG))
                    (iv as IKasbonLunasView).onSuccessKasbonLunas(value as KasbonLunasResponseModel)
            }


            override fun onError(e: Throwable) {
                LogHelper(TAG, e.localizedMessage).run()
                iv.handleError(ResponseHelper.validateMessageError(e.message
                        ?: App.context!!.getString(R.string.error_global)))
            }
        })
    }

    override fun onKasbonCustomer(data: KasbonCustomerRequestModel, activity: BaseActivity) {

        KasbonDao(this).onKasbonCustomer(data, activity).subscribe(object : ObserverApi(activity) {


            override fun onNext(value: BaseResponse) {
                if (ResponseHelper.validateResponse(value, iv, TAG))
                    (iv as IKasbonCustomerView).onSuccessKasbonCustomer(value as KasbonCustomerResponseModel)
            }


            override fun onError(e: Throwable) {
                LogHelper(TAG, e.localizedMessage).run()
                iv.handleError(ResponseHelper.validateMessageError(e.message
                        ?: App.context!!.getString(R.string.error_global)))
            }
        })

    }

    override fun onKasbonCustomerDetail(data: KasbonCustomerDetailRequestModel, activity: BaseActivity) {

        KasbonDao(this).onKasbonCustomerDetail(data, activity).subscribe(object : ObserverApi(activity) {

            override fun onNext(value: BaseResponse) {
                if (ResponseHelper.validateResponse(value, iv, TAG))
                    (iv as IKasbonCustomerDetailView).onSuccessKasbonCustomerDetail(value as KasbonAktifSelectedResponseModel)
            }


            override fun onError(e: Throwable) {
                LogHelper(TAG, e.localizedMessage).run()
                iv.handleError(ResponseHelper.validateMessageError(e.message
                        ?: App.context!!.getString(R.string.error_global)))
            }
        })
    }

    override fun onKasbonReport(data: KasbonReportRequestModel, activity: BaseActivity) {

        KasbonDao(this).onKasbonReport(data, activity).subscribe(object : ObserverApi(activity) {

            override fun onNext(value: BaseResponse) {
                if (ResponseHelper.validateResponse(value, iv, TAG))
                    (iv as IKasbonReportView).onSuccessKasbonReport(value as KasbonReportResponseModel)
            }


            override fun onError(e: Throwable) {
                LogHelper(TAG, e.localizedMessage).run()
                iv.handleError(ResponseHelper.validateMessageError(e.message
                        ?: App.context!!.getString(R.string.error_global)))
            }
        })
    }

    override fun onKasbonExport(data: KasbonExportRequestModel, activity: BaseActivity) {

        KasbonDao(this).onKasbonExport(data, activity).subscribe(object : ObserverApi(activity) {

            override fun onNext(value: BaseResponse) {
                if (ResponseHelper.validateResponse(value, iv, TAG))
                    (iv as IKasbonExportView).onSuccessKasbonExport(value as KasbonExportResponseModel)
            }


            override fun onError(e: Throwable) {
                LogHelper(TAG, e.localizedMessage).run()
                iv.handleError(ResponseHelper.validateMessageError(e.message
                        ?: App.context!!.getString(R.string.error_global)))
            }
        })
    }

    override fun onKasbonAktifSelected(data: KasbonAktifSelectedRequestModel, activity: BaseActivity) {

        KasbonDao(this).onKasbonAktifSelected(data, activity).subscribe(object : ObserverApi(activity) {

            override fun onNext(value: BaseResponse) {
                if (ResponseHelper.validateResponse(value, iv, TAG))
                    (iv as IKasbonAktifSelectedView).onSuccessKasbonAktifSelected(value as KasbonAktifSelectedResponseModel)
            }


            override fun onError(e: Throwable) {
                LogHelper(TAG, e.localizedMessage).run()
                iv.handleError(ResponseHelper.validateMessageError(e.message
                        ?: App.context!!.getString(R.string.error_global)))
            }
        })
    }

    override fun onKasbonCashPayment(data: KasbonCashPaymentRequestModel, activity: BaseActivity) {

        KasbonDao(this).onKasbonCashPayment(data, activity).subscribe(object : ObserverApi(activity) {

            override fun onNext(value: BaseResponse) {
                if (ResponseHelper.validateResponse(value, iv, TAG))
                    (iv as IKasbonCashPaymentView).onSuccessKasbonCashPayment(value as KasbonCashPaymentResponseModel)
            }


            override fun onError(e: Throwable) {
                LogHelper(TAG, e.localizedMessage).run()
                iv.handleError(ResponseHelper.validateMessageError(e.message
                        ?: App.context!!.getString(R.string.error_global)))
            }
        })
    }

    override fun onKasbonQrPaymentGenerate(data: KasbonQrPaymentRequestModel, activity: BaseActivity) {

        KasbonDao(this).onKasbonQrPaymentGenerate(data, activity).subscribe(object : ObserverApi(activity) {

            override fun onNext(value: BaseResponse) {
                //if (ResponseHelper.validateResponse(value, iv, TAG))
                if (value.baseMeta.code == 401 || value.baseMeta.code == 498) {
                    (iv as IKasbonQrPaymentView).logout()
                    iv.handleError(value.baseMeta.message)
                } else {
                    (iv as IKasbonQrPaymentView).onSuccessGenerateQrKasbon(value as KasbonQrPaymentResponseModel)
                }
            }


            override fun onError(e: Throwable) {
                //(iv as IKasbonQrPaymentView).onBadConnectionGenerateQr()
                LogHelper(TAG, e.localizedMessage).run()
                iv.handleError(ResponseHelper.validateMessageError(e.message
                        ?: App.context!!.getString(R.string.error_global)))
            }
        })
    }

    override fun onKasbonQrPaymentStatus(data: KasbonQrPaymentStatusRequestModel, activity: BaseActivity) {

        KasbonDao(this).onKasbonQrPaymentStatus(data, activity).subscribe(object : ObserverApi(activity) {

            override fun onNext(value: BaseResponse) {
                if (ResponseHelper.validateResponse(value, iv, TAG))
                    (iv as IKasbonQrPaymentView).onSuccessCheckStatusQrKasbon(value as KasbonQrPaymentStatusResponseModel)
            }


            override fun onError(e: Throwable) {
                LogHelper(TAG, e.localizedMessage).run()
                iv.handleError(ResponseHelper.validateMessageError(e.message
                        ?: App.context!!.getString(R.string.error_global)))
            }
        })
    }

    override fun onKasbonQrPaymentStatusAuto(data: KasbonQrPaymentStatusRequestModel, count: Int, activity: BaseActivity) {
        val currentCount = count + 1

        KasbonDao(this).onKasbonQrPaymentStatus(data, activity).subscribe(object : ObserverApi(activity) {


            override fun onNext(value: BaseResponse) {
                val result = value as KasbonQrPaymentStatusResponseModel
                val statusKasbon = result.data?.status_cashbond

                if (statusKasbon!!.contains("Active") || statusKasbon.contains("Belum Lunas") && count < 9) {
                    val handler = Handler()
                    handler.postDelayed({
                        onKasbonQrPaymentStatusAuto(data, currentCount, activity)
                    }, 2000)
                } else if (statusKasbon.contains("Active") || statusKasbon.contains("Belum Lunas") && count >= 9) {
                    (iv as IKasbonQrPaymentView).handleCheckComplete()
                } else {
                    (iv as IKasbonQrPaymentView).onSuccessCheckStatusQrKasbon(value as KasbonQrPaymentStatusResponseModel)
                }
            }


            override fun onError(e: Throwable) {
                (iv as IKasbonQrPaymentView).onBadConnectionGenerateQr()
                /*LogHelper(TAG, e.localizedMessage).run()
                iv.handleError(ResponseHelper.validateMessageError(e.message
                        ?: App.context!!.getString(R.string.error_global)))*/
            }
        })
    }


    /**
     * Interfaces
     * */
    interface IKasbonQrPaymentView : ViewBaseInterface {
        fun onSuccessGenerateQrKasbon(result: KasbonQrPaymentResponseModel)
        fun onSuccessCheckStatusQrKasbon(result: KasbonQrPaymentStatusResponseModel)

        //fun handleProcessing()
        fun handleCheckComplete()
        fun handleCheckProcessing()
        fun onBadConnectionGenerateQr()
        fun onBadConnectionCheckStatus()
        //fun handleCheckComplete()
        //override fun handleError(s: String)
    }

    interface IKasbonCashPaymentView : ViewBaseInterface {
        fun onSuccessKasbonCashPayment(result: KasbonCashPaymentResponseModel)
    }

    interface IKasbonAktifView : ViewBaseInterface {
        fun onSuccessKasbonAktif(result: KasbonAktifResponseModel)
        fun onDuplicateSelected()
    }

    interface IKasbonAktifSelectedView : ViewBaseInterface {
        fun onSuccessKasbonAktifSelected(result: KasbonAktifSelectedResponseModel)
    }

    interface IKasbonLunasView : ViewBaseInterface {
        fun onSuccessKasbonLunas(result: KasbonLunasResponseModel)
    }

    interface IKasbonCustomerDetailView : ViewBaseInterface {
        fun onSuccessKasbonCustomerDetail(result: KasbonAktifSelectedResponseModel)
    }

    interface IKasbonCustomerView : ViewBaseInterface {
        fun onSuccessKasbonCustomer(result: KasbonCustomerResponseModel)
    }

    interface IKasbonReportView : ViewBaseInterface {
        fun onSuccessKasbonReport(result: KasbonReportResponseModel)
    }

    interface IKasbonExportView : ViewBaseInterface {
        fun onSuccessKasbonExport(result: KasbonExportResponseModel)
    }
}