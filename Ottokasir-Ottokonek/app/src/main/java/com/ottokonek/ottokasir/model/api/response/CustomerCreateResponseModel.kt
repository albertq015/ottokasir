package com.ottokonek.ottokasir.model.api.response

import app.beelabs.com.codebase.base.response.BaseResponse
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class CustomerCreateResponseModel : BaseResponse() {
    var data: DataBean? = null
    var links: Any? = null
//    var meta: MetaBean? = null

    class DataBean {
        var id = 0
        var name: String? = null
        var phone: String? = null
        var email: String? = null
        var merchant_id = 0
        var merchant: String? = null

    }

//    class MetaBean {
//        var isStatus = false
//        var code = 0
//        var message: String? = null
//
//    }
}