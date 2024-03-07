package com.ottokonek.ottokasir.model.api.request

import com.ottokonek.ottokasir.model.miscModel.ItemModel
import java.io.Serializable

class QrPaymentGenerateRequestModel : Serializable {

    var customer_id: Int = 0
    var total_amount = 0.0
    var bill_ref_num: String? = null
    var date: String? = null
    var customerName: String? = null
    var issuer: String? = null
    var products: ArrayList<ItemModel>? = null
    var mid: String = ""
    var mpan: String = ""
    var nmId: String = ""

    constructor()
}
