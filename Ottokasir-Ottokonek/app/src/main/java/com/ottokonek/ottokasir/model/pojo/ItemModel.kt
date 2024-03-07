package com.ottokonek.ottokasir.model.pojo

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class ItemModel : Serializable {
    var id: Int = 0
    var discount: String = ""
    var price: String = ""
    var quantity: Int = 0
    var invoice_id: Int = 0
    var item_name: String? = ""
    var item_code: String? = ""
    var product_price = ""
    var product_type: String? = ""
    var product_id: Int = 0
}