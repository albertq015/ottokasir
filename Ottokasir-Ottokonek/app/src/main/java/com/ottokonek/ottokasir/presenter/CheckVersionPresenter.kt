package com.ottokonek.ottokasir.presenter

import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.contract.IView
import app.beelabs.com.codebase.base.response.BaseResponse
import com.ottokonek.ottokasir.App
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.base.BasePresenterApp
import com.ottokonek.ottokasir.base.ObserverApi
import com.ottokonek.ottokasir.model.api.request.CheckVersionRequestModel
import com.ottokonek.ottokasir.model.api.response.CheckVersionResponseModel
import com.ottokonek.ottokasir.model.dao.CheckVersionDao
import com.ottokonek.ottokasir.ui.callback.ViewBaseInterface
import com.ottokonek.ottokasir.utils.LogHelper
import com.ottokonek.ottokasir.utils.ResponseHelper

class CheckVersionPresenter(iv: IView) : BasePresenterApp(iv), CheckVersionDao.ICheckVersion {

    private val TAG = CheckVersionPresenter::class.java.simpleName

    override fun onCheckVersion(data: CheckVersionRequestModel, activity: BaseActivity) {

        CheckVersionDao(this).onCheckVersion(data, activity).subscribe(object : ObserverApi(activity) {

            override fun onNext(value: BaseResponse) {
                if (ResponseHelper.validateResponse(value, iv, TAG))
                    (iv as ICheckVersionView).onSuccessCheckVersion(value as CheckVersionResponseModel)
            }


            override fun onError(e: Throwable) {
                LogHelper(TAG, e.localizedMessage).run()
                iv.handleError(ResponseHelper.validateMessageError(e.message
                        ?: App.context!!.getString(R.string.error_global)))
            }
        })

    }

    interface ICheckVersionView : ViewBaseInterface {
        fun onSuccessCheckVersion(result: CheckVersionResponseModel)
    }
}