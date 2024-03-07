package com.ottokonek.ottokasir.model.db

import io.realm.RealmObject

open class AddressBean : RealmObject(){

    var province_id: Long = 0
    var city_id: Long = 0
    var districts_id: Long = 0
    var villages_id: Long = 0
    var province_name: String? = null
    var city_name: String? = null
    var complete_address: String? = null
    var address_name: String? = null

}