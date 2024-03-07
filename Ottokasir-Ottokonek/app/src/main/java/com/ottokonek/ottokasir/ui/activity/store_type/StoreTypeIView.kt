package com.ottokonek.ottokasir.ui.activity.store_type

import com.ottokonek.ottokasir.model.api.response.StoreTypeListResponseModel
import com.ottokonek.ottokasir.model.api.response.StoreTypeProductAddResponseModel
import com.ottokonek.ottokasir.model.api.response.StoreTypeProductResponseModel
import com.ottokonek.ottokasir.ui.callback.ViewBaseInterface

interface StoreTypeIView : ViewBaseInterface {

    fun onSuccessGetStoreTypeList(data: StoreTypeListResponseModel)
    fun onSuccessGetStoreTypeProduct(data: StoreTypeProductResponseModel)
    fun onSuccessGetStoreTypeProductAdd(data: StoreTypeProductAddResponseModel)
    fun duplicateMasterProduct()
    fun sessionExpired()
    fun unSelectedItem(productsIds: ArrayList<Int>)
    //fun allSelectedItem(allSelectProduct: ArrayList<Int>)
    fun selectedItem(productsIds: ArrayList<Int>)
}