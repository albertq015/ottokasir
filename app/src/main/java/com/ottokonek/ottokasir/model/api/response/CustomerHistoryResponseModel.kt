package com.ottokonek.ottokasir.model.api.response

import app.beelabs.com.codebase.base.response.BaseResponse
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class CustomerHistoryResponseModel : BaseResponse() {

    var links: Any? = null
    var meta: MetaBean? = null
    var data: List<DataBean>? = null

    class MetaBean {

        var isStatus = false
        var code = 0
        var message: String? = null

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class DataBean {

        var id = 0
        var orderID: String? = null
        var paymentType: String? = null
        var amount = 0
        var totalPaid = 0
        var change = 0
        var referenceNumber: String? = null
        var issuer: String? = null
        var customerName: String? = null
        var customerPhone: String? = null
        var transactionDate: String? = null
        var status: String? = null
        var transactionDirection: String? = null
        var refundStatus: String? = null
        var refundRrn: String? = null
        var products: String? = null

    }
}