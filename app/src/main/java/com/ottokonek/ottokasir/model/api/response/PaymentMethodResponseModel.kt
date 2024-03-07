package com.ottokonek.ottokasir.model.api.response

import app.beelabs.com.codebase.base.response.BaseResponse
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.ottokonek.ottokasir.model.miscModel.PaymentMethodModel

@JsonIgnoreProperties(ignoreUnknown = true)
class PaymentMethodResponseModel : BaseResponse() {
    var link: com.ottokonek.ottokasir.model.miscModel.LinksModel? = null
    var data: PaymentMethodDataResponseModel? = null

    @JsonIgnoreProperties(ignoreUnknown = true)
    class PaymentMethodDataResponseModel {
        @JsonProperty("payment_methods")
        var paymentMethods: List<PaymentMethodModel>? = null
    }
}