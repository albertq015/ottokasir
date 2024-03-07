package com.ottokonek.ottokasir.model.api.request

class QrPaymentCheckStatRequestModel {

    /**
     * qr_data : response from generate qr
     */


    var bill_ref_num: String? = null

    constructor(bill_ref_num: String?) {
        this.bill_ref_num = bill_ref_num
    }


}
