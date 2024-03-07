package com.ottokonek.ottokasir.model.db

import com.ottokonek.ottokasir.model.api.response.TransactionHistoryResponse
import io.realm.RealmObject

open class TransactionModel : RealmObject() {
    /**
     * id : 1
     * orderID : 1111111
     * paymentType : Cash
     * amount : 100000
     * referenceNumber : 123124142122
     * customerPhone :
     * transactionDate : 2019-06-26T15:20:10Z
     * status : Success
     * products :
     */

    var id: Int = 0
    var orderID: String? = null
    var paymentType: String? = null
    var amount = 0.0
    var totalPaid = 0.0
    var change = 0.0
    var referenceNumber: String? = null
    var customerPhone: String? = null
    var transactionDate: String? = null
    var customerName: String = ""
    var issuer: String = ""
    var status: String? = null
    var transactionDirection: String? = null
    var products: String? = null
    var refundStatus: String = ""
    var refundRrn: String = ""

    fun changeToParcelable(): TransactionHistoryResponse.DataBean {
        val data = TransactionHistoryResponse.DataBean()
        data.id = this.id
        data.orderID = this.orderID
        data.paymentType = this.paymentType
        data.amount = this.amount
        data.referenceNumber = this.referenceNumber
        data.customerPhone = this.customerPhone
        data.transactionDate = this.transactionDate
        data.status = this.status
        data.transactionDirection = this.transactionDirection
        data.products = this.products
        data.customerName = this.customerName
        data.change = this.change
        data.issuer = this.issuer
        data.totalPaid = this.totalPaid
        data.refundStatus = this.refundStatus
        data.refundRrn = this.refundRrn

        return data
    }
}