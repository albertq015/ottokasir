package com.ottokonek.ottokasir.ui.activity.auth

import com.ottokonek.ottokasir.model.api.response.LoginResponseModel
import com.ottokonek.ottokasir.ui.callback.ViewBaseInterface

interface LoginViewInterface : ViewBaseInterface {

    fun handleProcessing()
    fun handleDataLogin(model: LoginResponseModel)
    fun handleDataLoginSync(model: LoginResponseModel)
    fun handleError()
}