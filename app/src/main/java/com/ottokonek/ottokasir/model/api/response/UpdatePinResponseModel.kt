package com.ottokonek.ottokasir.model.api.response

import app.beelabs.com.codebase.base.response.BaseResponse
import app.beelabs.com.codebase.base.response.DefaultMetaResponse
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class UpdatePinResponseModel : BaseResponse() {
    var data: DataResponseModel? = null
    var meta: DefaultMetaResponse? = null

    @JsonIgnoreProperties(ignoreUnknown = true)
    class DataResponseModel {

    }

}