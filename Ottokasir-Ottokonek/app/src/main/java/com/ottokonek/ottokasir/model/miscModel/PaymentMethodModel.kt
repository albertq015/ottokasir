package com.ottokonek.ottokasir.model.miscModel

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class PaymentMethodModel {
    var code: String? = null
    var name: String?  = null
    var description : String? = null
}
