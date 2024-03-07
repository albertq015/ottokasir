package com.ottokonek.ottokasir.model.api.response

import app.beelabs.com.codebase.base.response.BaseResponse
import app.beelabs.com.codebase.base.response.DefaultMetaResponse
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class NotificationResponseModel : BaseResponse() {

    lateinit var data: ArrayList<DataResponseModel>
    lateinit var meta: DefaultMetaResponse

    @JsonIgnoreProperties(ignoreUnknown = true)
    class DataResponseModel {

        var id: Int = 0
        var notif_type: String? = null
        var title: String? = null
        var body: String? = null
        var user_id: Int = 0
        var created_at: String? = null

    }
}