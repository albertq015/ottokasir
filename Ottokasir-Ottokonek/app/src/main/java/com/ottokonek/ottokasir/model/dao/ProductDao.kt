package com.ottokonek.ottokasir.model.dao

import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BaseDao
import com.ottokonek.ottokasir.model.api.Api
import com.ottokonek.ottokasir.model.api.request.LoginSyncRequest
import com.ottokonek.ottokasir.model.api.request.ProductListRequestModel
import com.ottokonek.ottokasir.model.api.response.LoginResponseModel
import com.ottokonek.ottokasir.model.api.response.ProductlistResponseModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ProductDao : BaseDao() {

    interface IProductDao {
        fun onGetProduct(isFiltered: Boolean,page: String, query: String, mode: ProductListRequestModel)
        fun syncLogin(data: LoginSyncRequest, activity: BaseActivity)
        fun onDestroy()
        fun onClear()

    }

    fun onGetProduct(page: String, query: String, model: ProductListRequestModel): Observable<ProductlistResponseModel> {
        return Api.doGetProductlist(page, query, model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun doLoginSync(data: LoginSyncRequest,activity: BaseActivity):Observable<LoginResponseModel>{
        return Api.doLoginSync(data,activity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

}