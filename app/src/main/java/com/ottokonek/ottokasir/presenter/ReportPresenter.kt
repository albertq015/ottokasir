package com.ottokonek.ottokasir.presenter

import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BasePresenter
import app.beelabs.com.codebase.base.contract.IView
import app.beelabs.com.codebase.support.rx.RxObserver
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.model.api.request.ExportTransactionRequestModel
import com.ottokonek.ottokasir.model.api.request.HistoryRequestModel
import com.ottokonek.ottokasir.model.api.response.TransactionExportResponseModel
import com.ottokonek.ottokasir.model.api.response.TransactionReportResponse
import com.ottokonek.ottokasir.model.dao.ReportDao
import com.ottokonek.ottokasir.ui.callback.ViewBaseInterface
import com.ottokonek.ottokasir.ui.dialog.InfoDialog
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class ReportPresenter(val iView: IView) : BasePresenter(), ReportDao.IReportDao {

    private var reportIView: IView
    private lateinit var dialogInfo: InfoDialog
    private val compositeDisposable: CompositeDisposable


    init {
        this.reportIView = iView
        compositeDisposable = CompositeDisposable()
    }

    override fun getTransactionReport(model: HistoryRequestModel, activity: BaseActivity) {
        dialogInfo = InfoDialog(activity, R.style.CustomDialog, activity.resources.getString(R.string.info_logout), true)
        ReportDao(this).onReportTransaction(model, activity).subscribe(object : RxObserver<TransactionReportResponse>(reportIView, null) {
            override fun onSubscribe(d: Disposable) {
                super.onSubscribe(d)
                compositeDisposable.add(d)
            }

            override fun onNext(o: Any) {
                super.onNext(o)
                o as TransactionReportResponse
                if (o.meta.code == 200) {
                    (iView as ITransactionReportIView).onSuccessTransactionReport(o)
                } else if (o.meta.code == 498 || o.meta.code == 401) {
                    (iView as ITransactionReportIView).hideLoading()
                    dialogInfo.show()
                    (iView as ITransactionReportIView).logout()
                } else {
                    (iView as ITransactionReportIView).handleError(o.meta?.message!!)
                }
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                (iView as ITransactionReportIView).handleError(e!!.localizedMessage)
            }
        })
    }

    override fun onExportTransaction(model: ExportTransactionRequestModel, activity: BaseActivity) {
        ReportDao(this).onExportTransaction(model, activity).subscribe(
                object : RxObserver<TransactionExportResponseModel>(reportIView, null) {

                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        compositeDisposable.clear()
                    }

                    override fun onNext(o: Any) {
                        super.onNext(o)
                        o as TransactionExportResponseModel
                        if (o.meta?.code == 200) {
                            (reportIView as ITransactionReportIView).onSuccessTransactionExport(o)
                        }
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        (reportIView as ITransactionReportIView).handleError(e!!.localizedMessage)
                    }

                })
    }

    override fun onDestroy() {
        compositeDisposable.clear()
    }

    /**
     * Interface
     * */
    interface ITransactionReportIView : ViewBaseInterface {
        fun onSuccessTransactionReport(data: TransactionReportResponse)
        fun onSuccessTransactionExport(data: TransactionExportResponseModel)
    }


}