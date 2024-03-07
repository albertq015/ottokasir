package com.ottokonek.ottokasir.model.api.request

class CreateProductRequestModel {

    /**
     * name : Cuci Bedcover a
     * category : 1
     * price : 50000
     * stocks : 0
     * master_product_id :
     * barcode : 1122334455668
     */

    var name: String? = null
    var image: String? = null
    var category = 0
    var price = 0.0
    var stocks = 0
    var stock_alert = 0
    var show_alert = false
    var stock_active = false
    var master_product_id: String? = null
    var barcode: String? = null
}