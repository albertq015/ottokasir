package com.ottokonek.ottokasir.model.api.response

import app.beelabs.com.codebase.base.response.BaseResponse
import com.ottokonek.ottokasir.model.miscModel.CheckStocksitemReplyModel

class CheckStocksResponseModel : BaseResponse() {
    
    var data: List<CheckStocksitemReplyModel>? = null;
    var links: com.ottokonek.ottokasir.model.miscModel.LinksModel? = null


}