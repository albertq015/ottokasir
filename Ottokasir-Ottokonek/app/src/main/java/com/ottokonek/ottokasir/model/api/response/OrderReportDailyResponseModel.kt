package com.ottokonek.ottokasir.model.api.response

import app.beelabs.com.codebase.base.response.BaseResponse
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.ottokonek.ottokasir.model.pojo.ReportDailyModel

@JsonIgnoreProperties(ignoreUnknown = true)
class OrderReportDailyResponseModel : BaseResponse() {

    var data: DataResponseModel? = null

    @JsonIgnoreProperties(ignoreUnknown = true)
    class DataResponseModel {

        var orders: ArrayList<ReportDailyModel>? = null
        @JsonProperty("total_trans")
        var totalTrans : Int = 0
        @JsonProperty("total_qty")
        var totalQty : Double = 0.0
        @JsonProperty("total_paid")
        var totalPaid : Double = 0.0
    }
}