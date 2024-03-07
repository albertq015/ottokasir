package com.ottokonek.ottokasir.model.miscModel

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)

class CheckStocksitemReplyModel() {

    @JsonProperty("item_code")
    var itemCode: String? = null
    var availability: String? = null
    var error: String? = null

}
