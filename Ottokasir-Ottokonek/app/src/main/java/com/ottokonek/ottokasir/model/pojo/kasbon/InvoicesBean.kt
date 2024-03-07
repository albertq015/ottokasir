package com.ottokonek.ottokasir.model.pojo.kasbon

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class InvoicesBean : Serializable {

    var id = 0
    var customer_code: String? = null
    var invoice_code: String? = null
    var status: String? = null
    var warehouse_name: String? = null
    var order_id = 0
}