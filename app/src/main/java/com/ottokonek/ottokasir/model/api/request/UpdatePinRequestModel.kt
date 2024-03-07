package com.ottokonek.ottokasir.model.api.request

class UpdatePinRequestModel {
    var phone: String? = null
    var email: String? = null
    var current_password: String? = null
    var password: String? = null
    var password_confirmation: String? = null

    constructor(phone: String?, email: String?, current_password: String?, password: String?, password_confirmation: String?) {
        this.phone = phone
        this.email = email
        this.current_password = current_password
        this.password = password
        this.password_confirmation = password_confirmation
    }
}