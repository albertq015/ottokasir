package com.ottokonek.ottokasir.model.pojo

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class ReportDailyModel {
    var id: Int = 0
    var code: String? = null
    var order_date: String? = null
    var total_amount: Int = 0
    var total_paid: Double = 0.0

}