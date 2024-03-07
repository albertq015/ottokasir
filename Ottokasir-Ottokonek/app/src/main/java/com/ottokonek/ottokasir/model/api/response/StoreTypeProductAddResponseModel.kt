package com.ottokonek.ottokasir.model.api.response

import app.beelabs.com.codebase.base.response.BaseResponse
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class StoreTypeProductAddResponseModel : BaseResponse() {

    var links: LinksBean? = null

    @JsonIgnoreProperties(ignoreUnknown = true)
    var data: List<DataBean>? = null

    class LinksBean

    @JsonIgnoreProperties(ignoreUnknown = true)
    class DataBean {

        var id = 0
        var name: String? = null
        var isActive = false
        var barcode: String? = null

    }
}