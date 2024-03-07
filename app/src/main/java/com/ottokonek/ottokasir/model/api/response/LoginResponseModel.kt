package com.ottokonek.ottokasir.model.api.response

import app.beelabs.com.codebase.base.response.BaseResponse
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.ottokonek.ottokasir.model.db.AddressBean
import com.ottokonek.ottokasir.model.db.LoginSyncBean
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class LoginResponseModel : BaseResponse() {
    var data: UserDataResponseModel? = null

    @JsonIgnoreProperties(ignoreUnknown = true)
    class UserDataResponseModel : Serializable {
        var user_id: Int = 0
        var access_token: String? = null
        var merchant_id: String? = null
        var name: String? = null
        var merchant_name: String? = null
        var email: String? = null
        var avatar: String? = null
        var avatar_thumb: String? = null
        var latitude: Double = 0.toDouble()
        var longitude: Double = 0.toDouble()
        var phone: String? = null
        var secondary_phone: String? = null
        var business_category_name: String? = null
        var business_type_name: String? = null
        var wallet_id: Long = 0
        var mid: String? = null
        var mpan: String? = null
        var nmId: String? = null
        var address: Address = Address()

        var store_type_id = 0
        var store_type_name: String? = null
        var customer_code: String? = null
        var warehouse_code: String? = null
        var payment_method_code: String? = null
        var financial_account_code: String? = null


        @JsonIgnoreProperties(ignoreUnknown = true)
        class Address : Serializable {
            var province_id: Long = 0
            var city_id: Long = 0
            var districts_id: Long = 0
            var villages_id: Long = 0
            var province_name: String? = ""
            var city_name: String? = ""
            var complete_address: String? = ""
            var address_name: String? = ""
        }

        fun changeToRealm(): LoginSyncBean {
            var loginSyncRequest = LoginSyncBean()

            loginSyncRequest.access_token = access_token
            loginSyncRequest.avatar = avatar
            loginSyncRequest.avatar_thumb = avatar_thumb
            loginSyncRequest.business_category_name = business_category_name
            loginSyncRequest.business_type_name = business_type_name
            loginSyncRequest.user_id = user_id
            loginSyncRequest.email = email
            loginSyncRequest.firebase_token = com.ottokonek.ottokasir.IConfig.FIREBASE_TOKEN
            loginSyncRequest.latitude = latitude
            loginSyncRequest.longitude = longitude
            loginSyncRequest.merchant_id = merchant_id
            loginSyncRequest.merchant_name = merchant_name
            loginSyncRequest.name = name
            loginSyncRequest.phone = phone
            loginSyncRequest.secondary_phone = secondary_phone
            loginSyncRequest.wallet_id = wallet_id
            loginSyncRequest.nmId = nmId
            loginSyncRequest.mpan = mpan
            loginSyncRequest.mid = mid

            var mAddress = AddressBean()
            mAddress.address_name = address?.address_name
            mAddress.city_id = address?.city_id!!
            mAddress.city_name = address?.city_name
            mAddress.complete_address = address?.complete_address
            mAddress.districts_id = address.districts_id
            mAddress.province_id = address.province_id
            mAddress.province_name = address?.province_name
            mAddress.villages_id = address.villages_id
            loginSyncRequest.address = mAddress


            return loginSyncRequest
        }
    }
}
