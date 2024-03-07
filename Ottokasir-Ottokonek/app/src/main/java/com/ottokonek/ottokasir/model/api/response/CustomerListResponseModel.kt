package com.ottokonek.ottokasir.model.api.response

import app.beelabs.com.codebase.base.response.BaseResponse
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class CustomerListResponseModel : BaseResponse() {
    var data: DataBean? = null
    var links: Any? = null
//    var meta: MetaBean? = null

    class DataBean {
        var merchant_customers: MutableList<MerchantCustomersBean>? = null

        class MerchantCustomersBean {
            var id = 0
            var name = ""
            var phone = ""
            var email = ""
            var merchant_id = 0
            var merchant = ""

        }
    }

//    class MetaBean {
//        var isStatus = false
//        var code = 0
//        var message: String? = null
//
//    }
}