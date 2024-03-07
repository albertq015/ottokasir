package com.ottokonek.ottokasir.model.api.request

//data class ProductListRequestModel(var phone : String)
data class ProductListRequestModel(var sorting: String, var keyword: String, var active: String)