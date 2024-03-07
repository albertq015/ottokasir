package com.ottokonek.ottokasir.model.pojo.kasbon

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable
import java.util.ArrayList

@JsonIgnoreProperties(ignoreUnknown = true)
class OrderBean : Serializable {

    var id = 0
    var code: String? = null
    var status: String? = null
    var store_name: String? = null
    var merchant: String? = null
    var customer_name: String? = null
    var customer_phone: String? = null
    var total_paid: String? = null
    var payment_method: String? = null
    var total_amount: String? = null
    var store_discount: String? = null
    var paid_nominal: String? = null
    var change: String? = null
    var total_items = 0
    var order_date: String? = null
    var cashbond_payment_date: String? = null
    var status_cashbond: String? = null
    var active_cashbond: String? = null
    var customer_id = 0
    var details: Any? = null
    var invoices: List<InvoicesBean>? = null
    var items: ArrayList<ItemsBean>? = null
}