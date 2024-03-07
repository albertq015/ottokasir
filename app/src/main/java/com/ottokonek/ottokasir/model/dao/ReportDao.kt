package com.ottokonek.ottokasir.model.dao

import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BaseDao
import com.ottokonek.ottokasir.model.api.Api
import com.ottokonek.ottokasir.model.api.request.ExportTransactionRequestModel
import com.ottokonek.ottokasir.model.api.request.HistoryRequestModel
import com.ottokonek.ottokasir.model.api.response.TransactionExportResponseModel
import com.ottokonek.ottokasir.model.api.response.TransactionReportResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ReportDao(dao: IReportDao) : BaseDao() {

    interface IReportDao {
        fun getTransactionReport(model: HistoryRequestModel, activity: BaseActivity)
        fun onExportTransaction(model: ExportTransactionRequestModel, activity: BaseActivity)
        fun onDestroy()
    }


    fun onReportTransaction(model: HistoryRequestModel, activity: BaseActivity): Observable<TransactionReportResponse> {
        return Api.doGetTransactionReport(model, activity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun onExportTransaction(model: ExportTransactionRequestModel, activity: BaseActivity): Observable<TransactionExportResponseModel> {
        return Api.onExportTransaction(model, activity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}