package com.ottokonek.ottokasir.model.api.response

import app.beelabs.com.codebase.base.response.BaseResponse
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class KasbonCustomerResponseModel : BaseResponse() {

    var links: Any? = null
//    var meta: MetaBean? = null
    var data: List<DataBean>? = null

//    class MetaBean {
//
//        var isStatus = false
//        var code = 0
//        var message: String? = null
//
//    }

    class DataBean : Serializable {

        var id: Int? = null
        var customer_name: String? = null
        var customer_phone: String? = null
        var total_cashbond_active: String? = null

    }
}