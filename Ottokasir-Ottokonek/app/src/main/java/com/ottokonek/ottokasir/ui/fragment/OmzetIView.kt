package com.ottokonek.ottokasir.ui.fragment

import com.ottokonek.ottokasir.model.api.response.AvailableOmzetResponseModel
import com.ottokonek.ottokasir.model.api.response.DailyOmzetResponseModel
import com.ottokonek.ottokasir.ui.callback.ViewBaseInterface

interface OmzetIView : ViewBaseInterface {

    fun setDailyOmzet(model: DailyOmzetResponseModel)
    fun setAvailableOmzet(model: AvailableOmzetResponseModel)
    override fun handleError(s : String)
}