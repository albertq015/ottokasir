package com.ottokonek.ottokasir.model.api.response

import app.beelabs.com.codebase.base.response.BaseResponse
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty


@JsonIgnoreProperties(ignoreUnknown = true)
class StocksResponseModel : BaseResponse() {

    var data: StocksDataResponseModel? = null
    var links: com.ottokonek.ottokasir.model.miscModel.LinksModel? = null


    @JsonIgnoreProperties(ignoreUnknown = true)
    class StocksDataResponseModel {
        @JsonProperty("stock_count")
        var stockCount: Int = 0
        @JsonProperty("item_count")
        var itemCount: Int = 0

        var stocks: List<com.ottokonek.ottokasir.model.miscModel.StockItemModel>? = null
    }

}