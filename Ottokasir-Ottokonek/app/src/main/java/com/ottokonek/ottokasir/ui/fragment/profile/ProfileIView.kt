package com.ottokonek.ottokasir.ui.fragment.profile

import com.ottokonek.ottokasir.model.api.response.ResetStoreTypeResponseModel
import com.ottokonek.ottokasir.ui.callback.ViewBaseInterface

interface ProfileIView : ViewBaseInterface {

    fun onSuccessResetStoreType(result: ResetStoreTypeResponseModel)
}