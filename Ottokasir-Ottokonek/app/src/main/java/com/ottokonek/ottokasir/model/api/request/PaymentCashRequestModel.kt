package com.ottokonek.ottokasir.model.api.request

class PaymentCashRequestModel() {

    /**
     * items : [{"quantity":2,"stock_id":"27"}]
     * payment_type : Cash
     * total_amount : 30000
     * total_paid : 30000
     * change : 0
     */

    var items: List<ItemsBean>? = null
    var payment_type: String? = null
    var total_amount: String? = null
    var total_paid: String? = null
    var change: String? = null
    var customer_id = 0

    class ItemsBean {
        /**
         * quantity : 2
         * stock_id : 27
         */
        var quantity = 0
        var stock_id: String? = null

        constructor(quantity: Int, stock_id: String?) {
            this.quantity = quantity
            this.stock_id = stock_id
        }

        constructor()
    }
}
