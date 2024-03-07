package com.ottokonek.ottokasir.model.miscModel

import java.io.Serializable

data class ItemModel(
        val stock_id: Int?,
        val amount: Double?,
        val name: String?,
        val qty: Int?

) : Serializable
