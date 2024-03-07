package com.ottokonek.ottokasir.model.api.response

import app.beelabs.com.codebase.base.response.BaseResponse
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class KasbonReportResponseModel : BaseResponse() {

    var data: DataBean? = null
    var links: Any? = null
//    var meta: MetaBean? = null

    class DataBean {

        var totalAmount: String? = null
        var jenisTransaksi: List<JenisTransaksiBean>? = null

        class JenisTransaksiBean {

            var name: String? = null
            var qty: String? = null
            var amount: String? = null

        }
    }

//    class MetaBean {
//
//        var isStatus = false
//        var code = 0
//        var message: String? = null
//
//    }
}