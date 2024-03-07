package com.ottokonek.ottokasir.ui.fragment.manage_product

import com.ottokonek.ottokasir.model.miscModel.ProductItemModel
import com.ottokonek.ottokasir.ui.callback.ViewBaseInterface

interface ManageProductIView : ViewBaseInterface {

    fun loadProducts(models: List<ProductItemModel?>?)
    fun getProductWithFilter(query: String)

    fun setStatusAPI(status: Boolean)

    fun getStatusAPI(): Boolean
    fun showClearSearch()
    fun hideClearSearch()
}