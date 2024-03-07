package com.ottokonek.ottokasir.model.pojo.kasbon

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class ItemsBean : Serializable{
    var id = 0
    var discount: String? = null
    var price: String? = null
    var product_price: String? = null
    var quantity = 0
    var invoice_id = 0
    var item_name: String? = null
    var item_code: String? = null
    var product_type: String? = null
    var product_id = 0
}