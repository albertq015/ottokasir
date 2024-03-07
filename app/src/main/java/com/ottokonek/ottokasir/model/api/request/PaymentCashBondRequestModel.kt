package com.ottokonek.ottokasir.model.api.request

class PaymentCashBondRequestModel {

    var items: List<ItemsBean>? = null
    var payment_type: String? = null
    var total_amount: String? = null
    var customer_id = 0

    class ItemsBean {
        var quantity = 0
        var stock_id: String? = null

        constructor(quantity: Int, stock_id: String?) {
            this.quantity = quantity
            this.stock_id = stock_id
        }

        constructor()

    }
}