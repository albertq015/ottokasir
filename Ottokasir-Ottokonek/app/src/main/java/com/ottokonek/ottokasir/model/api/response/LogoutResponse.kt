package com.ottokonek.ottokasir.model.api.response

import app.beelabs.com.codebase.base.response.BaseResponse
import app.beelabs.com.codebase.base.response.DefaultMetaResponse

class LogoutResponse : BaseResponse() {


    /**
     * meta : {"status":false,"code":498,"message":"Session aplikasi anda telah berakhir"}
     */

    var meta: DefaultMetaResponse? = null
}
