package com.ottokonek.ottokasir.model.api.response

import app.beelabs.com.codebase.base.response.BaseResponse

class BarcodeResponseModel : BaseResponse() {

    var data: com.ottokonek.ottokasir.model.miscModel.ProductItemModel? = null;
    var links: com.ottokonek.ottokasir.model.miscModel.LinksModel? = null
}