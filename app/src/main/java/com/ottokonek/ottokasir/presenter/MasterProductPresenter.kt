package com.ottokonek.ottokasir.presenter

import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BasePresenter
import app.beelabs.com.codebase.base.contract.IView
import app.beelabs.com.codebase.support.rx.RxObserver
import com.ottokonek.ottokasir.model.api.request.MasterProductRequestModel
import com.ottokonek.ottokasir.model.api.response.MasterProductResponseModel
import com.ottokonek.ottokasir.model.dao.MasterProductDao
import com.ottokonek.ottokasir.ui.activity.edit_product.MasterProductIView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class MasterProductPresenter(iView: IView) : BasePresenter(), MasterProductDao.IMasterProduct {

    var masterProductIView: IView = iView
    val compositeDisposable: CompositeDisposable = CompositeDisposable()


    override fun onMasterProduct(data: MasterProductRequestModel, activity: BaseActivity) {

        MasterProductDao(this).onMasterProduct(data, activity).subscribe(object : RxObserver<MasterProductResponseModel>(masterProductIView, null) {

            override fun onSubscribe(d: Disposable) {
                super.onSubscribe(d)
                compositeDisposable.add(d)
            }

            override fun onNext(o: Any) {
                super.onNext(o)
                o as MasterProductResponseModel
                if (o.meta.code == 200) {
                    (masterProductIView as MasterProductIView).onSuccessMasterProduct(o as MasterProductResponseModel)
                } else {
                    (masterProductIView as MasterProductIView).onSuccessMasterProduct(o as MasterProductResponseModel)
                }
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                (masterProductIView as MasterProductIView).handleError(e!!.localizedMessage)
            }
        })

    }

    override fun onDestroy() {
        compositeDisposable.clear()
    }

}