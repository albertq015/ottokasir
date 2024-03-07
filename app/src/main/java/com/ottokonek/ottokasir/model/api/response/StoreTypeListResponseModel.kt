package com.ottokonek.ottokasir.model.api.response

import app.beelabs.com.codebase.base.response.BaseResponse
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class StoreTypeListResponseModel : BaseResponse() {
    /**
     * data : [{"id":1,"name":"Toko Pulsa","active":true},{"id":2,"name":"Warteg","active":true}]
     * links : {}
     * meta : {"status":true,"code":200,"message":"OK"}
     */
    var links: LinksBean? = null
//    var meta: MetaBean? = null
    var data: List<DataBean>? = null

    @JsonIgnoreProperties(ignoreUnknown = true)
    class DataBean : Serializable{
        /**
         * id : 1
         * name : Toko Pulsa
         * active : true
         */
        var id = 0
        var name: String? = null
        var isActive = false
    }

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
}