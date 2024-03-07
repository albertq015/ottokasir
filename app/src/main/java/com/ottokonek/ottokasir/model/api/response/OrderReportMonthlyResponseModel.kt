package com.ottokonek.ottokasir.model.api.response

import app.beelabs.com.codebase.base.response.BaseResponse
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.ottokonek.ottokasir.model.pojo.ReportMonthlyModel

@JsonIgnoreProperties(ignoreUnknown = true)
class OrderReportMonthlyResponseModel : BaseResponse() {

    var data: DataResponseModel? = null

    @JsonIgnoreProperties(ignoreUnknown = true)
    class DataResponseModel {

        var orders: ArrayList<ReportMonthlyModel>? = null
        var total_stored: Double? = 0.0
        var total_profit: Double? = 0.0
    }
}