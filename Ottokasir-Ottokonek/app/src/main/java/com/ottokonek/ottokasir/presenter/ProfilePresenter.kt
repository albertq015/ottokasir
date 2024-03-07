package com.ottokonek.ottokasir.presenter

import android.content.Context
import android.location.Geocoder
import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BasePresenter
import app.beelabs.com.codebase.base.contract.IView
import app.beelabs.com.codebase.support.rx.RxObserver
import com.ottokonek.ottokasir.model.api.request.ResetStoreTypeRequestModel
import com.ottokonek.ottokasir.model.api.response.ProfileResponseModel
import com.ottokonek.ottokasir.model.api.response.ResetStoreTypeResponseModel
import com.ottokonek.ottokasir.model.dao.ProfileDao
import com.ottokonek.ottokasir.ui.fragment.profile.ProfileIView
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class ProfilePresenter(iView: IView) : BasePresenter(), ProfileDao.IProfileDao {

    var profileIView: IView = iView
    val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun getProfile(activity: BaseActivity): Observable<ProfileResponseModel> {
        return ProfileDao(this).getProfileDAO()
    }

    override fun onResetStoreType(data: ResetStoreTypeRequestModel, activity: BaseActivity) {
        ProfileDao(this).onResetStoreType(data, activity).subscribe(
                object : RxObserver<ResetStoreTypeResponseModel>(profileIView, null) {

                    override fun onSubscribe(d: Disposable?) {
                        super.onSubscribe(d)
                        compositeDisposable.clear()
                    }

                    override fun onNext(o: Any?) {
                        super.onNext(o)
                        o as ResetStoreTypeResponseModel
                        if (o.meta?.code == 200) {
                            (profileIView as ProfileIView).onSuccessResetStoreType(o)
                        } else {
                            (profileIView as ProfileIView).onSuccessResetStoreType(o)
                        }
                    }

                    override fun onError(e: Throwable?) {
                        super.onError(e)
                        (profileIView as ProfileIView).handleError(e!!.localizedMessage)
                    }

                })
    }


    fun getAddressFromLatlng(latitude: Double, longitude: Double, context: Context): String {
        var geocoder: Geocoder = Geocoder(context)
        var address = geocoder.getFromLocation(latitude, longitude, 1)
        if (address!!.size != 0) {
            return address!!.get(0).getAddressLine(0)

        }
        return ""
    }

}