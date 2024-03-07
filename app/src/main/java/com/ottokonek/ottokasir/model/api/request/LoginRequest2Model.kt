package com.ottokonek.ottokasir.model.api.request

import app.beelabs.com.codebase.base.response.BaseResponse
import app.beelabs.com.codebase.base.response.DefaultMetaResponse

class LoginRequest2Model : BaseResponse() {


    /**
     * data : {"user_id":14,"access_token":"ZYgSNCRfWAnfeSHSaweqplTKpyNCzslE","merchant_id":"MG6700000170","name":"Toko Reza","merchant_name":"","email":"rezawardana@gmail.com","avatar":"http://ottopay2-api.clappingape.com/uploads/avatar/14/14_avatar_image.jpg","avatar_thumb":"http://ottopay2-api.clappingape.com/uploads/avatar/14/thumb_14_avatar_image.jpg","latitude":-6.239372044801712,"longitude":106.8173258844763,"phone":"082217322093","secondary_phone":"","business_category_name":"","business_type_name":"","wallet_id":4,"address":{"province_id":31,"city_id":3171,"districts_id":3171070,"villages_id":3171070004,"province_name":"DKI JAKARTA","city_name":"KOTA JAKARTA SELATAN","complete_address":"Jl Dendapasar 85 Jakarta","address_name":"Alamat Reza"}}
     * meta : {"status":true,"code":200,"message":"Succesful"}
     */

    var data: DataBean? = null
    var meta: DefaultMetaResponse? = null

    class DataBean {
        /**
         * user_id : 14
         * access_token : ZYgSNCRfWAnfeSHSaweqplTKpyNCzslE
         * merchant_id : MG6700000170
         * name : Toko Reza
         * merchant_name :
         * email : rezawardana@gmail.com
         * avatar : http://ottopay2-api.clappingape.com/uploads/avatar/14/14_avatar_image.jpg
         * avatar_thumb : http://ottopay2-api.clappingape.com/uploads/avatar/14/thumb_14_avatar_image.jpg
         * latitude : -6.239372044801712
         * longitude : 106.8173258844763
         * phone : 082217322093
         * secondary_phone :
         * business_category_name :
         * business_type_name :
         * wallet_id : 4
         * address : {"province_id":31,"city_id":3171,"districts_id":3171070,"villages_id":3171070004,"province_name":"DKI JAKARTA","city_name":"KOTA JAKARTA SELATAN","complete_address":"Jl Dendapasar 85 Jakarta","address_name":"Alamat Reza"}
         */

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
        var wallet_id: Int = 0
        var address: AddressBean? = null

        class AddressBean {
            /**
             * province_id : 31
             * city_id : 3171
             * districts_id : 3171070
             * villages_id : 3171070004
             * province_name : DKI JAKARTA
             * city_name : KOTA JAKARTA SELATAN
             * complete_address : Jl Dendapasar 85 Jakarta
             * address_name : Alamat Reza
             */

            var province_id: Int = 0
            var city_id: Int = 0
            var districts_id: Int = 0
            var villages_id: Long = 0
            var province_name: String? = null
            var city_name: String? = null
            var complete_address: String? = null
            var address_name: String? = null
        }
    }
}
