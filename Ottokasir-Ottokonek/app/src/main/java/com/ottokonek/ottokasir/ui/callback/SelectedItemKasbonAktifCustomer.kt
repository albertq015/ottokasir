package com.ottokonek.ottokasir.ui.callback

import java.util.ArrayList

interface SelectedItemKasbonAktifCustomer {

    fun callbackSelectedKasbonCustomer(orderIdsKasbon: ArrayList<String>, amountOrder: String)
}