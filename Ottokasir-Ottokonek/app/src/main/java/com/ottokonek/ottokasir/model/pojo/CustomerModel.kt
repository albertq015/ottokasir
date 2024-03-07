package com.ottokonek.ottokasir.model.pojo

import java.io.Serializable

class CustomerModel : Serializable {

    var id: Int = -1
    var initialName: String = ""
    var fullName: String = ""
    var noHandphone: String = ""

    constructor(id: Int, initialName: String, fullName: String, noHandphone: String) {
        this.id = id
        this.initialName = initialName
        this.fullName = fullName
        this.noHandphone = noHandphone
    }

    constructor(id: Int, fullName: String, noHandphone: String) {
        this.id = id
        this.fullName = fullName
        this.noHandphone = noHandphone
    }
}