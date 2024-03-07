package com.ottokonek.ottokasir.model.api.response

import app.beelabs.com.codebase.base.response.BaseResponse
import app.beelabs.com.codebase.base.response.DefaultMetaResponse
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class ProfileResponseModel : BaseResponse() {
    var meta: DefaultMetaResponse? =  null
    var data: DataResponseModel? = null


    @JsonIgnoreProperties(ignoreUnknown = true)
    class DataResponseModel {
        var name: String? = null
        var join_at: String? = null
        var phone: String? = null
        var address: String? = null
        var in_charge: String? = null
        var avatar: String? = null
    }

}