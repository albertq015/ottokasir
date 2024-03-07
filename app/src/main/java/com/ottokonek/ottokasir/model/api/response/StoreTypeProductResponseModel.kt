package com.ottokonek.ottokasir.model.api.response

import app.beelabs.com.codebase.base.response.BaseResponse
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable
import kotlin.collections.ArrayList

@JsonIgnoreProperties(ignoreUnknown = true)
class StoreTypeProductResponseModel : BaseResponse() {
    /**
     * data : [{"id":2,"name":"Pulsa XL 100ribu","active":true,"barcode":"8998009010231","store_type_id":0,"store_type_name":""}]
     * links : {}
     * meta : {"status":true,"code":200,"message":"OK"}
     */
    var links: LinksBean? = null
//    var meta: MetaBean? = null
    var data: ArrayList<DataBean>? = null

    class LinksBean
//    class MetaBean {
//        /**
//         * status : true
//         * code : 200
//         * message : OK
//         */
//        var isStatus = false
//        var code = 0
//        var message: String? = null
//
//    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class DataBean : Serializable {
        /**
         * id : 2
         * name : Pulsa XL 100ribu
         * active : true
         * barcode : 8998009010231
         */
        var id = -1
        var name: String? = null
        var isSelected = false
        var barcode: String? = null

    }
}