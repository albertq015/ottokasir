package com.ottokonek.ottokasir.dao.productList

import app.beelabs.com.codebase.base.BaseDao
import com.ottokonek.ottokasir.model.api.Api
import com.ottokonek.ottokasir.model.api.request.ProductListRequestModel
import com.ottokonek.ottokasir.model.api.response.ProductlistResponseModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ProductlistInteractor() : BaseDao(){

    //    private Context vContext;
    //
    //    public ProductlistInteractor(Context vContext) {
    //        this.vContext = vContext;
    //    }

    fun callProductlistInteractor(page: String,query: String,model:ProductListRequestModel): Observable<ProductlistResponseModel> {
        return Api.Companion.doGetProductlist(page,query,model).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    fun callProductlistInteractor(categ: String, page: String, perPage: String, query: String,model:ProductListRequestModel): Observable<ProductlistResponseModel> {
        return Api.doGetProductlist(page,query,model).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }
}
