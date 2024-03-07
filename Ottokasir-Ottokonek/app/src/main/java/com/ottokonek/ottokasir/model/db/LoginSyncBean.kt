package com.ottokonek.ottokasir.model.db

import io.realm.RealmObject

open class LoginSyncBean : RealmObject(){

    var user_id: Int = 0
    var access_token: String? = null
    var merchant_id: String? = null
    var name: String? = null
    var merchant_name: String? = null
    var email: String? = null
    var avatar: String? = null
    var avatar_thumb: String? = null
    var firebase_token: String? = null
    var latitude: Double = 0.toDouble()
    var longitude: Double = 0.toDouble()
    var phone: String? = null
    var secondary_phone: String? = null
    var business_category_name: String? = null
    var business_type_name: String? = null
    var wallet_id: Long = 0
    var address: AddressBean? = null
    var mid: String? = null
    var mpan: String? = null
    var nmId: String? = null


}