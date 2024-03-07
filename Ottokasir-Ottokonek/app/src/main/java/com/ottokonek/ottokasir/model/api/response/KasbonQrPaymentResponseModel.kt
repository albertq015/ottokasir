package com.ottokonek.ottokasir.model.api.response

import app.beelabs.com.codebase.base.response.BaseResponse
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class KasbonQrPaymentResponseModel : BaseResponse() {

    var data: DataBean? = null
    var links: Any? = null
//    var meta: MetaBean? = null

    class DataBean : Serializable {

        var qr_string: String? = null
        var mid: String? = null
        var mpan: String? = null
        var nmid: String? = null
        var merchant_name: String? = null
        var total_amount: Double? = null

    }

//    class MetaBean {
//
//        var isStatus = false
//        var code = 0
//        var message: String? = null
//
//    }
}