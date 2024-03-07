package com.ottokonek.ottokasir.base

import android.content.Context
import app.beelabs.com.codebase.base.response.BaseResponse
import com.ottokonek.ottokasir.ui.dialog.loading.CustomProgressDialog
import com.ottokonek.ottokasir.utils.LogHelper
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

open class ObserverApi(context: Context?, val isShowProgress: Boolean) : Observer<BaseResponse> {

    private val TAG = ObserverApi::class.java.simpleName
    private var context: Context? = null

    constructor(context: Context?) : this(context, false) {
        this.context = context
    }

    override fun onComplete() {
        showProgress(context, true)
    }

    override fun onNext(value: BaseResponse) {

    }

    override fun onSubscribe(d: Disposable) {
        showProgress(context, true)
    }

    override fun onError(e: Throwable) {
        showProgress(context, false)

        e?.let {
            LogHelper(TAG, it.message).run()
        }
    }

    protected fun showProgress(context: Context?, isShow: Boolean) {
        if (isShowProgress)
            context?.let {
                if (isShow)
                    CustomProgressDialog.showDialog(context, "")
                else
                    CustomProgressDialog.closeDialog()
            }
    }
}