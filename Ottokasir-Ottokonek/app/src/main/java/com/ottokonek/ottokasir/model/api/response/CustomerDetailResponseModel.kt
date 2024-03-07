package com.ottokonek.ottokasir.model.api.response

import app.beelabs.com.codebase.base.response.BaseResponse
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class CustomerDetailResponseModel : BaseResponse() {
    var data: DataBean? = null
    var links: Any? = null
//    var meta: MetaBean? = null

    class DataBean : Serializable {
        var merchant_customers: MerchantCustomersBean? = null

        class MerchantCustomersBean : Serializable {
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