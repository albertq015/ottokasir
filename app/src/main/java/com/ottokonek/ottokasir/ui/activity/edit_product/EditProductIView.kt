package com.ottokonek.ottokasir.ui.activity.edit_product

import app.beelabs.com.codebase.base.contract.IView
import com.ottokonek.ottokasir.model.api.response.CreateProductResponse
import com.ottokonek.ottokasir.model.api.response.DeleteProductResponse
import com.ottokonek.ottokasir.model.api.response.UpdateProductResponse

interface EditProductIView : IView {

    fun onDuplicateProduct(message: String)
    fun onSuccessCreateProduct(data: CreateProductResponse)
    fun onSuccessUpdateProduct(data: UpdateProductResponse)
    fun onSuccessDeleteProduct(data: DeleteProductResponse)
    fun onUpdateStatusNameAndPrice(isNameNotEmpty: Boolean, isPriceNotEmpty: Boolean)
}