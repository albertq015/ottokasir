package com.ottokonek.ottokasir.model.api.response

import app.beelabs.com.codebase.base.response.BaseResponse
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class AvailableOmzetResponseModel: BaseResponse() {

    /**
     * data : {"all_omset":"IDR 9,609,241","daily_omset":0}
     * meta : {"status":true,"code":200,"message":"OK"}
     */

    var data: DataBean? = null
    var meta: MetaBean? = null

    @JsonIgnoreProperties(ignoreUnknown = true)
    class DataBean {
        /**
         * all_omset : IDR 9,609,241
         * daily_omset : 0
         */

        var omzet: String? = null
        var omzetDaily: String? = null
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class MetaBean {
        /**
         * status : true
         * code : 200
         * message : OK
         */

        var isStatus: Boolean = false
        var code: Int = 0
        var message: String? = null
    }
}
