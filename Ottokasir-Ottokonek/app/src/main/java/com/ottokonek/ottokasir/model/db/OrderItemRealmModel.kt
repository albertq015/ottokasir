package com.ottokonek.ottokasir.model.db

import java.io.Serializable

import io.realm.RealmObject

open class OrderItemRealmModel : RealmObject(), Serializable {

    var type: String? = null
    var merchant_name: String? = null
    var amount: Int = 0
    var reference_number: String? = null
    var description: String? = null
    var date: Int = 0
    var status: String? = null
    var direction: String? = null
    var payment_method: String? = null
    var no_resi: String? = null
    var biller_reference: String? = null
    var stroom_token: String? = null
    var customer_id: String? = null
    var customerName: String? = null
    var commission: String? = null
}
