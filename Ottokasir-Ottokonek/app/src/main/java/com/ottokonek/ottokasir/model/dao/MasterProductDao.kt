package com.ottokonek.ottokasir.model.dao

import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BaseDao
import com.ottokonek.ottokasir.model.api.Api
import com.ottokonek.ottokasir.model.api.request.MasterProductRequestModel
import com.ottokonek.ottokasir.model.api.response.MasterProductResponseModel
import com.ottokonek.ottokasir.presenter.MasterProductPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MasterProductDao(dao: MasterProductPresenter) : BaseDao() {

    interface IMasterProduct {
        fun onMasterProduct(data: MasterProductRequestModel, activity: BaseActivity)
        fun onDestroy()
    }

    fun onMasterProduct(data: MasterProductRequestModel, activity: BaseActivity): io.reactivex.Observable<MasterProductResponseModel> {
        return Api.onMasterProduct(data, activity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}