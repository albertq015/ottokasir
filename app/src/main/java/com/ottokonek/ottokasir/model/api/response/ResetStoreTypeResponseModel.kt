package com.ottokonek.ottokasir.model.api.response

import app.beelabs.com.codebase.base.response.BaseResponse
import app.beelabs.com.codebase.base.response.DefaultMetaResponse
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class ResetStoreTypeResponseModel : BaseResponse() {
    /**
     * data : {"id":41,"phone":"085600000002","store_type_id":0}
     * links : null
     * meta : {"status":true,"code":200,"message":"OK"}
     */
    var data: DataBean? = null
    var links: Any? = null
    var meta: DefaultMetaResponse? = null

    class DataBean {
        /**
         * id : 41
         * phone : 085600000002
         * store_type_id : 0
         */
        var id = 0
        var phone: String? = null
        var store_type_id = 0

    }
}