package com.ottokonek.ottokasir.model.pojo

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class InvoiceModel : Serializable {
    var id: Int = 0
    var warehouse_code: String? = ""
    var customer_code: String? = ""
    var invoice_code: String? = ""
    var order_id: Int? = 0
    var created_at: String? = ""
}