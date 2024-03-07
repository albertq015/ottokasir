package com.ottokonek.ottokasir.ui.activity.edit_product

import com.ottokonek.ottokasir.model.api.response.MasterProductResponseModel
import com.ottokonek.ottokasir.ui.callback.ViewBaseInterface

interface MasterProductIView : ViewBaseInterface {

    fun onSuccessMasterProduct(data: MasterProductResponseModel)
    fun onSelectedProduct(data: MasterProductResponseModel.DataBean)
}