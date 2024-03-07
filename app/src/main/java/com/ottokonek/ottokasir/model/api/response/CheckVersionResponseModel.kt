package com.ottokonek.ottokasir.model.api.response

import app.beelabs.com.codebase.base.response.BaseResponse
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class CheckVersionResponseModel : BaseResponse() {

    var data: DataBean? = null
    var links: LinksBean? = null

    class DataBean {
        var version_app = 0
        var version_api = 0
        var force_update = false

    }

    class LinksBean
}