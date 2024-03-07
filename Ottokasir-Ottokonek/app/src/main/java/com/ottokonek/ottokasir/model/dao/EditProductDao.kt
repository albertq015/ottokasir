package com.ottokonek.ottokasir.model.dao

import android.widget.EditText
import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BaseDao
import com.ottokonek.ottokasir.model.api.Api
import com.ottokonek.ottokasir.model.api.request.CreateProductRequestModel
import com.ottokonek.ottokasir.model.api.request.DeleteProductRequestModel
import com.ottokonek.ottokasir.model.api.request.ProductListRequestModel
import com.ottokonek.ottokasir.model.api.request.UpdateProductRequestModel
import com.ottokonek.ottokasir.model.api.response.CreateProductResponse
import com.ottokonek.ottokasir.model.api.response.DeleteProductResponse
import com.ottokonek.ottokasir.model.api.response.UpdateProductResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class EditProductDao() : BaseDao() {

    interface IManageProduct {
        fun onCreateProduct(data: CreateProductRequestModel, activity: BaseActivity)

        fun onUpdateProduct(data: UpdateProductRequestModel, activity: BaseActivity)

        fun onDeleteProduct(data: DeleteProductRequestModel, activity: BaseActivity)

        fun onGetProduct(isFiltered: Boolean, page: String, query: String, mode: ProductListRequestModel)

        fun onValidateNameAndPrice(editTextName: EditText, editTextPrice: EditText)

        fun onDestroy()
    }

    fun onCreateProduct(data: CreateProductRequestModel, activity: BaseActivity): Observable<CreateProductResponse> {
        return Api.onCreateProduct(data, activity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun onUpdateProduct(data: UpdateProductRequestModel, activity: BaseActivity): Observable<UpdateProductResponse> {
        return Api.onUpdateProduct(data, activity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun onDeleteProduct(data: DeleteProductRequestModel, activity: BaseActivity): Observable<DeleteProductResponse> {
        return Api.onDeleteProduct(data, activity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}