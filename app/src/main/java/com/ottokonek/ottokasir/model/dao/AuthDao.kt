package com.ottokonek.ottokasir.model.dao

import android.app.Activity
import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BaseDao
import com.ottokonek.ottokasir.model.api.Api
import com.ottokonek.ottokasir.model.api.request.LoginRequestModel
import com.ottokonek.ottokasir.model.api.request.LoginSyncRequest
import com.ottokonek.ottokasir.model.api.response.LoginResponseModel
import com.ottokonek.ottokasir.model.api.response.LogoutResponse
import com.ottokonek.ottokasir.presenter.AuthPresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AuthDao(dao: AuthPresenter) : BaseDao() {

    interface IAuthDao {
        fun doLogin(data: LoginRequestModel, activity: BaseActivity)
        fun doLogout(activity: Activity)
        fun doLoginSync(data: LoginSyncRequest, activity: BaseActivity)
        fun onDestroy()
    }

    fun getLoginDAO(data: LoginRequestModel, activity: BaseActivity): Observable<LoginResponseModel> {
        return Api.onLogin(data, activity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun getLoginSync(data: LoginSyncRequest, activity: BaseActivity): Observable<LoginResponseModel> {
        return Api.doLoginSync(data, activity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun getLogoutDAO(): Observable<LogoutResponse> {
        return Api.onLogout()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

}