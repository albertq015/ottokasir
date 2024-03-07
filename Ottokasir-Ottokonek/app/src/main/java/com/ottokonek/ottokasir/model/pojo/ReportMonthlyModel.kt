package com.ottokonek.ottokasir.model.pojo

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class ReportMonthlyModel {
    var order_date: Long? = 0
    var total_amount: Double? = 0.0
    var total_paid: Double? = 0.0
}