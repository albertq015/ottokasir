package com.ottokonek.ottokasir.model.dao

import android.content.Context
import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BaseDao
import app.beelabs.com.codebase.base.contract.IDaoPresenter
import com.ottokonek.ottokasir.model.api.Api
import com.ottokonek.ottokasir.model.api.request.StoreTypeListRequestModel
import com.ottokonek.ottokasir.model.api.request.StoreTypeProductAddRequestModel
import com.ottokonek.ottokasir.model.api.request.StoreTypeProductRequestModel
import com.ottokonek.ottokasir.model.api.response.StoreTypeListResponseModel
import com.ottokonek.ottokasir.model.api.response.StoreTypeProductAddResponseModel
import com.ottokonek.ottokasir.model.api.response.StoreTypeProductResponseModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class StoreTypeDao() : BaseDao() {

    constructor(dao: IStoreType?) : this()

    interface IStoreType : IDaoPresenter {
        fun onGetStoreTypeList(data: StoreTypeListRequestModel, activity: BaseActivity)
        fun onGetStoreTypeProduct(data: StoreTypeProductRequestModel, activity: BaseActivity)
        fun onGetsToreTypeProductAdd(data: StoreTypeProductAddRequestModel, activity: BaseActivity)
    }

    fun onGetStoreTypeList(data: StoreTypeListRequestModel, context: Context): Observable<StoreTypeListResponseModel> = Api.onGetStoreTypeList(data, context)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun onGetStoreTypeProduct(data: StoreTypeProductRequestModel, context: Context): Observable<StoreTypeProductResponseModel> = Api.onGetStoreTypeProduct(data, context)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun onGetStoreTypeProductAdd(data: StoreTypeProductAddRequestModel, context: Context): Observable<StoreTypeProductAddResponseModel> = Api.onGetStoreTypeProductAdd(data, context)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}