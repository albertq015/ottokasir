package com.ottokonek.ottokasir.presenter

import android.app.Activity
import android.util.Log
import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BasePresenter
import app.beelabs.com.codebase.base.contract.IView
import app.beelabs.com.codebase.support.rx.RxObserver
import com.ottokonek.ottokasir.model.api.request.LoginRequestModel
import com.ottokonek.ottokasir.model.api.request.LoginSyncRequest
import com.ottokonek.ottokasir.model.api.response.LoginResponseModel
import com.ottokonek.ottokasir.model.api.response.LogoutResponse
import com.ottokonek.ottokasir.model.dao.AuthDao
import com.ottokonek.ottokasir.ui.activity.auth.LoginViewInterface
import com.ottokonek.ottokasir.utils.ActionUtil
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class AuthPresenter(iView: IView) : BasePresenter(), AuthDao.IAuthDao {

    var loginViewInterface: IView = iView
    val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun doLogin(data: LoginRequestModel, activity: BaseActivity) {
        AuthDao(this).getLoginDAO(data, activity).subscribe(object : RxObserver<LoginResponseModel>(loginViewInterface, null) {

            override fun onSubscribe(d: Disposable) {
                super.onSubscribe(d)
                compositeDisposable.add(d)
            }

            override fun onNext(o: Any) {
                o as LoginResponseModel
                if (o.baseMeta.code == 200) {
                    (loginViewInterface as LoginViewInterface).handleDataLogin(o)
                } else {
                    (loginViewInterface as LoginViewInterface).handleDataLogin(o)
                }
            }

            override fun onError(e: Throwable) {
                Log.e("Login Error", e.toString())
                (loginViewInterface as LoginViewInterface).onConnectionFailed(e!!.localizedMessage)
            }

            override fun onComplete() {
                super.onComplete()
                (loginViewInterface as LoginViewInterface).hideLoading()
            }
        })


    }

    override fun doLogout(activity: Activity) {
        AuthDao(this).getLogoutDAO()
                .subscribe(object : RxObserver<LogoutResponse>(loginViewInterface, null) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        compositeDisposable.add(d)

                    }

                    override fun onNext(o: Any) {
                        super.onNext(o)
                        ActionUtil.logoutAction(activity)
                    }
                }
                )
    }


    override fun doLoginSync(data: LoginSyncRequest, activity: BaseActivity) {
        AuthDao(this).getLoginSync(data, activity).subscribe(object : RxObserver<LoginResponseModel>(loginViewInterface, null) {

            override fun onSubscribe(d: Disposable) {
                super.onSubscribe(d)
                compositeDisposable.add(d)
            }

            override fun onNext(o: Any) {
                o as LoginResponseModel
                if (o.baseMeta.code == 200) {
                    (loginViewInterface as LoginViewInterface).handleDataLoginSync(o as LoginResponseModel)
                } else {
                    (loginViewInterface as LoginViewInterface).handleDataLoginSync(o as LoginResponseModel)
                }
            }

            override fun onError(e: Throwable) {
                Log.e("Login Error", e.toString())
                (loginViewInterface as LoginViewInterface).handleError(e!!.localizedMessage)
            }

            override fun onComplete() {
                super.onComplete()
                (loginViewInterface as LoginViewInterface).hideLoading()
            }
        })


    }

    override fun onDestroy() {
        compositeDisposable.clear()
    }


}