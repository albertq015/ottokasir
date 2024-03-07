package com.ottokonek.ottokasir.dao.sync

import com.ottokonek.ottokasir.dao.manager.LocalDbManager
import com.ottokonek.ottokasir.model.api.request.LoginSyncRequest
import com.ottokonek.ottokasir.model.api.response.LoginResponseModel
import com.ottokonek.ottokasir.model.db.LoginSyncBean
import io.realm.Realm

object SyncManager {

    fun putSyncData(data: LoginResponseModel.UserDataResponseModel) {
        LocalDbManager.querryRealm { realm -> realm.delete(LoginSyncBean::class.java) }
        var realmData = data.changeToRealm()

        LocalDbManager.querryRealm { realm -> realm.copyToRealm<LoginSyncBean>(realmData) }
    }

    fun delSyncData() {
        LocalDbManager.querryRealm { realm ->
            realm.delete(LoginSyncBean::class.java)
        }
    }

    fun getSyncData(): LoginSyncBean? {
        val r = Realm.getDefaultInstance();
        val results = r.where(LoginSyncBean::class.java).findFirst()

        return results
    }

    fun changeFromRealm(data : LoginSyncBean): LoginSyncRequest {
        var loginSyncRequest = LoginSyncRequest()

        loginSyncRequest.access_token = data.access_token
        loginSyncRequest.avatar = data.avatar
        loginSyncRequest.avatar_thumb = data.avatar_thumb
        loginSyncRequest.business_category_name = data.business_category_name
        loginSyncRequest.business_type_name = data.business_type_name
        loginSyncRequest.user_id = data.user_id
        loginSyncRequest.email = data.email
        loginSyncRequest.firebase_token = data.firebase_token
        loginSyncRequest.latitude = data.latitude
        loginSyncRequest.longitude = data.longitude
        loginSyncRequest.merchant_id = data.merchant_id
        loginSyncRequest.merchant_name = data.merchant_name
        loginSyncRequest.name = data.name
        loginSyncRequest.phone = data.phone
        loginSyncRequest.secondary_phone = data.secondary_phone
        loginSyncRequest.wallet_id = data.wallet_id
        var mAddress= LoginSyncRequest.Address()
        mAddress.address_name = data.address?.address_name
        mAddress.city_id = data.address?.city_id!!
        mAddress.city_name = data.address?.city_name
        mAddress.complete_address = data.address?.complete_address
        mAddress.districts_id = data.address!!.districts_id
        mAddress.province_id = data.address!!.province_id
        mAddress.province_name = data.address?.province_name
        mAddress.villages_id = data.address!!.villages_id
        loginSyncRequest.address = mAddress
        loginSyncRequest.mid = data.mid
        loginSyncRequest.mpan = data.mpan
        loginSyncRequest.nmId = data.nmId


        return loginSyncRequest
    }
}