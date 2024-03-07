package com.ottokonek.ottokasir.presenter

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BasePresenter
import app.beelabs.com.codebase.base.response.BaseResponse
import app.beelabs.com.codebase.support.rx.RxObserver
import com.ottokonek.ottokasir.model.api.request.CreateProductRequestModel
import com.ottokonek.ottokasir.model.api.request.DeleteProductRequestModel
import com.ottokonek.ottokasir.model.api.request.ProductListRequestModel
import com.ottokonek.ottokasir.model.api.request.UpdateProductRequestModel
import com.ottokonek.ottokasir.model.api.response.CreateProductResponse
import com.ottokonek.ottokasir.model.api.response.DeleteProductResponse
import com.ottokonek.ottokasir.model.api.response.ProductlistResponseModel
import com.ottokonek.ottokasir.model.api.response.UpdateProductResponse
import com.ottokonek.ottokasir.model.dao.EditProductDao
import com.ottokonek.ottokasir.model.dao.ProductDao
import com.ottokonek.ottokasir.ui.callback.ViewBaseInterface
import com.ottokonek.ottokasir.ui.activity.edit_product.EditProductIView
import com.ottokonek.ottokasir.ui.fragment.manage_product.ManageProductIView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class EditProductPresenter(val mIView: ViewBaseInterface) : BasePresenter(), EditProductDao.IManageProduct {

    val callback: EditProductIView? = if (mIView is EditProductIView) mIView else null
    val compositeDisposable = CompositeDisposable()
    val dao = EditProductDao()
    private var isOnLoadingData = false

    private var isName = false
    private var isPrice = false
    private var isStock = false

    override fun onCreateProduct(data: CreateProductRequestModel, activity: BaseActivity) {
        dao.onCreateProduct(data, activity).subscribe(object : RxObserver<CreateProductResponse>
        (callback, null) {
            override fun onSubscribe(d: Disposable?) {
                super.onSubscribe(d)
                compositeDisposable.add(d)
            }

            override fun onNext(o: Any?) {
                super.onNext(o)
                o as CreateProductResponse
                if (o.meta.code == 200) {
                    callback?.onSuccessCreateProduct(o)
                } else if (o.meta.code == 401) {
                    mIView.onApiFailed("Error", o.meta.message)
                    mIView.logout()
                } else if (o.meta.code == 422) {
                    callback?.onDuplicateProduct(o.meta.message)
                } else {
                    mIView.onApiFailed("Error", o.meta.message)
                    mIView.hideLoading()
                }
            }

            override fun onError(e: Throwable?) {
                mIView.hideLoading()
                mIView.showToast(e!!.localizedMessage)
            }
        })
    }


    override fun onUpdateProduct(data: UpdateProductRequestModel, activity: BaseActivity) {
        dao.onUpdateProduct(data, activity).subscribe(object : RxObserver<UpdateProductResponse>
        (callback, null) {
            override fun onSubscribe(d: Disposable?) {
                super.onSubscribe(d)
                compositeDisposable.add(d)
            }

            override fun onNext(o: Any?) {
                super.onNext(o)
                o as UpdateProductResponse
                if (o.meta.code == 200) {
                    callback?.onSuccessUpdateProduct(o)
                } else if (o.meta.code == 401) {
                    mIView.onApiFailed("Error", o.meta.message)
                    mIView.logout()
                } else {
                    mIView.onApiFailed("Error", o.meta.message)
                    mIView.hideLoading()
                }
            }

            override fun onError(e: Throwable?) {
                mIView.hideLoading()
                mIView.showToast(e!!.localizedMessage)
            }
        })
    }

    override fun onDeleteProduct(data: DeleteProductRequestModel, activity: BaseActivity) {
        dao.onDeleteProduct(data, activity).subscribe(object : RxObserver<DeleteProductResponse>
        (callback, null) {
            override fun onSubscribe(d: Disposable?) {
                super.onSubscribe(d)
                compositeDisposable.add(d)
            }

            override fun onNext(o: Any?) {
                super.onNext(o)
                o as DeleteProductResponse
                if (o.meta.code == 200) {
                    callback?.onSuccessDeleteProduct(o)
                } else if (o.meta.code == 401) {
                    mIView.onApiFailed("Error", o.meta.message)
                    mIView.logout()
                } else {
                    mIView.onApiFailed("Error", o.meta.message)
                    mIView.hideLoading()
                }
            }

            override fun onError(e: Throwable?) {
                mIView.hideLoading()
                mIView.showToast(e!!.localizedMessage)
            }
        })
    }

    override fun onGetProduct(isFiltered: Boolean, page: String, query: String, model: ProductListRequestModel) {
        var message: String?

        if (isFiltered) message = "Loading"
        else message = null

        ProductDao().onGetProduct(page, query, model).subscribe(object : RxObserver<ProductlistResponseModel>(mIView, null) {
            override fun onSubscribe(d: Disposable?) {
                compositeDisposable.add(d)

                if (mIView is ManageProductIView)
                    mIView.setStatusAPI(false)
            }

            override fun onNext(o: Any?) {
                if (mIView is ManageProductIView) {
                    if ((o as BaseResponse).baseMeta.code == 200) {
                        mIView.loadProducts((o as ProductlistResponseModel).data.products)
                        mIView.hideLoading()
                    } else if (o.baseMeta.code == 401) {
                        mIView.onApiFailed("Error", o.baseMeta.message)
                        mIView.logout()
                    } else {
                        mIView.onApiFailed("Error", o.baseMeta.message)
                        mIView.hideLoading()
                    }
                }

            }

            override fun onComplete() {
                isOnLoadingData = false
                if (mIView is ManageProductIView) {
                    (mIView).setStatusAPI(true)
                }
            }

            override fun onError(e: Throwable?) {
                mIView.hideLoading()
                mIView.showToast(e!!.localizedMessage)
            }
        })
    }

    override fun onValidateNameAndPrice(editTextName: EditText, editTextPrice: EditText) {
        editTextName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (editTextName.text.toString().isNotEmpty()) {
                    isName = true
                    callback?.onUpdateStatusNameAndPrice(isName, isPrice)
                } else {
                    isName = false
                    callback?.onUpdateStatusNameAndPrice(isName, isPrice)
                }
            }

        })

        editTextPrice.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (editTextPrice.text.toString().isNotEmpty()) {
                    isPrice = true
                    callback?.onUpdateStatusNameAndPrice(isName, isPrice)
                } else {
                    isPrice = false
                    callback?.onUpdateStatusNameAndPrice(isName, isPrice)
                }
            }

        })
    }

    override fun onDestroy() {
        compositeDisposable.clear()
    }
}