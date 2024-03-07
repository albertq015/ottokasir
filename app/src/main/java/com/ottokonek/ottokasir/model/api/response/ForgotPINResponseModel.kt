package com.ottokonek.ottokasir.model.api.response

import app.beelabs.com.codebase.base.response.BaseResponse
import app.beelabs.com.codebase.base.response.DefaultMetaResponse
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class ForgotPINResponseModel : BaseResponse() {
    var meta: DefaultMetaResponse? =  null
//    var data: LoginResponseModel.UserDataResponseModel. = null


}