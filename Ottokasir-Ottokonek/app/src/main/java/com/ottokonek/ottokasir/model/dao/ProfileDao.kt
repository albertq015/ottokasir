package com.ottokonek.ottokasir.model.dao

import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BaseDao
import com.ottokonek.ottokasir.model.api.Api
import com.ottokonek.ottokasir.model.api.request.ResetStoreTypeRequestModel
import com.ottokonek.ottokasir.model.api.response.ProfileResponseModel
import com.ottokonek.ottokasir.model.api.response.ResetStoreTypeResponseModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ProfileDao(dao: IProfileDao) : BaseDao() {

    interface IProfileDao {
        fun getProfile(activity: BaseActivity): Observable<ProfileResponseModel>
        fun onResetStoreType(data: ResetStoreTypeRequestModel, activity: BaseActivity)
    }

    fun getProfileDAO(): Observable<ProfileResponseModel> {
        return Api.onProfile()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun onResetStoreType(data: ResetStoreTypeRequestModel, activity: BaseActivity): Observable<ResetStoreTypeResponseModel> {
        return Api.onResetStoreType(data, activity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }


}