package com.ottokonek.ottokasir.model.dao

import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BaseDao
import com.ottokonek.ottokasir.model.api.Api
import com.ottokonek.ottokasir.model.api.request.CheckVersionRequestModel
import com.ottokonek.ottokasir.model.api.response.CheckVersionResponseModel
import com.ottokonek.ottokasir.presenter.CheckVersionPresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CheckVersionDao(dao: CheckVersionPresenter) : BaseDao() {

    interface ICheckVersion {
        fun onCheckVersion(data: CheckVersionRequestModel, activity: BaseActivity)
    }

    fun onCheckVersion(data: CheckVersionRequestModel, activity: BaseActivity): Observable<CheckVersionResponseModel> {
        return Api.onCheckVersion(data, activity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

}