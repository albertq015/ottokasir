package com.ottokonek.ottokasir.presenter.productList

import android.util.Log
import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BasePresenter
import app.beelabs.com.codebase.base.contract.IView
import app.beelabs.com.codebase.base.response.BaseResponse
import app.beelabs.com.codebase.support.rx.RxObserver
import app.beelabs.com.codebase.support.util.CacheUtil
import com.ottokonek.ottokasir.IConfig
import com.ottokonek.ottokasir.dao.productList.ProductlistInteractor
import com.ottokonek.ottokasir.dao.sync.SyncManager
import com.ottokonek.ottokasir.model.api.request.LoginSyncRequest
import com.ottokonek.ottokasir.model.api.request.ProductListRequestModel
import com.ottokonek.ottokasir.model.api.response.LoginResponseModel
import com.ottokonek.ottokasir.model.api.response.ProductlistResponseModel
import com.ottokonek.ottokasir.model.dao.ProductDao
import com.ottokonek.ottokasir.model.db.ProductitemRealmModel
import com.ottokonek.ottokasir.ui.callback.ViewBaseInterface
import com.ottokonek.ottokasir.ui.activity.product_list.ProductlistViewInterface
import com.ottokonek.ottokasir.utils.MoneyUtil
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.realm.Realm

class ProductListPresenter(val iView: IView) : BasePresenter(), ProductDao.IProductDao {

    private var isFiltered = false

    private var isOnLoadingData = false
    private val compositeDisposable: CompositeDisposable

    private var view: ViewBaseInterface

    init {
        view = iView as ViewBaseInterface
        compositeDisposable = CompositeDisposable()
    }

    fun callProductlistData(page: String, query: String, model: ProductListRequestModel) {
        isOnLoadingData = true
        ProductlistInteractor().callProductlistInteractor(page, query, model).subscribe(object : RxObserver<ProductlistResponseModel>(iView, "Getting product..."
        ) {
            override fun onSubscribe(d: Disposable) {
                super.onSubscribe(d)
                compositeDisposable.add(d)
            }

            override fun onNext(o: Any) {
                if (iView is ProductlistViewInterface) {
                    if ((o as BaseResponse).baseMeta.code == 200) {
                        iView.clearProducts()
                        iView.loadProducts((o as ProductlistResponseModel).data.products)
                        if (o.data.products.size == 0) {
                            iView.showToast("Tidak ada produk ditemukan")
                        }
                        iView.hideLoading()
                    } else if (o.baseMeta.code == 401) {
                        iView.onApiFailed("Error", o.baseMeta.message)
                        iView.logout()
                    } else {
                        iView.onApiFailed("Error", o.baseMeta.message)
                        iView.hideLoading()
                    }
                }
            }

            override fun onComplete() {
                isOnLoadingData = false
            }

            override fun onError(e: Throwable) {
                view.hideLoading()
                view.onConnectionFailed(e!!.localizedMessage)
            }
        })
    }

    override fun onGetProduct(isFiltered: Boolean, page: String, query: String, model: ProductListRequestModel) {
        var message: String?

        if (isFiltered) message = "Loading"
        else message = null

        ProductDao().onGetProduct(page, query, model).subscribe(object : RxObserver<ProductlistResponseModel>(view, null) {
            override fun onSubscribe(d: Disposable) {
                compositeDisposable.add(d)

                if (iView is ProductlistViewInterface)
                    iView.setStatusAPI(false)
            }

            override fun onNext(o: Any) {
                if (iView is ProductlistViewInterface) {
                    o as ProductlistResponseModel
                    if ((o as BaseResponse).baseMeta.code == 200) {
                        iView.loadProducts(o.data.products)
                        iView.hideLoading()
                    } else if (o.baseMeta.code == 401) {
                        iView.onApiFailed("Error", o.baseMeta.message)
                        iView.logout()
                    }
                    /*else {
                        iView.onApiFailed("Error", o.baseMeta.message)
                        iView.hideLoading()
                    }*/
                }
            }

            override fun onComplete() {
                isOnLoadingData = false
                if (view is ProductlistViewInterface) {
                    (view as ProductlistViewInterface).setStatusAPI(true)
                }
            }

            override fun onError(e: Throwable) {
                view.hideLoading()
                view.onConnectionFailed(e!!.localizedMessage)
            }
        })
    }

    fun toggleFooter() {
        if (Realm.getDefaultInstance().where(ProductitemRealmModel::class.java).findFirst() != null) {
            var productCount = 0
            var total = 0.0
            var discount = 0.0
            for (realmModel in Realm.getDefaultInstance().where(ProductitemRealmModel::class.java).findAll()) {
                productCount += realmModel.count
                total += java.lang.Double.parseDouble(realmModel.price) * realmModel.count
                //discount += java.lang.Double.parseDouble(realmModel.discount) * realmModel.count
            }
            if (discount > 0) {
                (view as ProductlistViewInterface).addFooterData(productCount.toString() + "0",
                        MoneyUtil.convertIDRCurrencyFormat(total - discount),
                        MoneyUtil.convertIDRCurrencyFormat(total))
            } else {
                (view as ProductlistViewInterface).addFooterData(productCount.toString() + "0",
                        MoneyUtil.convertIDRCurrencyFormat(total), "0")
            }
            (view as ProductlistViewInterface).showFooter()
        } else {
            (view as ProductlistViewInterface).addFooterData("0", "0", "0")
            (view as ProductlistViewInterface).hideFooter()
        }


    }

    override fun syncLogin(data: LoginSyncRequest, activity: BaseActivity) {
        ProductDao().doLoginSync(data, activity).subscribe(object : RxObserver<LoginResponseModel>(view) {
            override fun onSubscribe(d: Disposable) {
                super.onSubscribe(d)
                compositeDisposable.add(d)

            }

            override fun onNext(o: Any) {
                if ((o as BaseResponse).baseMeta.code == 200) {
                    o as LoginResponseModel
                    CacheUtil.putPreferenceString(IConfig.SESSION_PAYMENT_CODE, o.data?.payment_method_code, activity);

                    CacheUtil.putPreferenceString(IConfig.SESSION_CUSTOMER_CODE, o.data?.customer_code, activity);
                    CacheUtil.putPreferenceString(IConfig.SESSION_WAREHOUSE_CODE, o.data?.warehouse_code, activity);
                    CacheUtil.putPreferenceString(IConfig.SESSION_FINANCIAL_ACC_CODE, o.data?.financial_account_code, activity);

                    CacheUtil.putPreferenceBoolean(IConfig.SESSION_IS_SYNC, true, activity)
                    SyncManager.delSyncData()
                    onGetProduct(false, "1", "", ProductListRequestModel(
                            "p_name desc", "", ""
                    ))

                    Log.d("SYNC Login", " SUCCESS")
                } else {
                    Log.d("SYNC Login", " SUCCESS")
                    (view as ProductlistViewInterface).hideProgressbar()
                }
            }

            override fun onError(e: Throwable) {
                (view as ProductlistViewInterface).hideProgressbar()
                view.onConnectionFailed(e!!.localizedMessage)
            }
        })
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
    }

    override fun onClear() {
        compositeDisposable.clear()
    }
}
