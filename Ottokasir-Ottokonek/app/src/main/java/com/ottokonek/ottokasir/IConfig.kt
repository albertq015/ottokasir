package com.ottokonek.ottokasir

import java.io.File

interface IConfig {
    companion object {


        val SESSION_TOKEN_CREDENTIAL = ""
        val FIREBASE_TOKEN = "cDBKTEH3qTM:APA91bEADGGA7NCTs-ijJI483i9HWBV38HHlEvr4LA6shI2Jhsf15UvBxlYyu1c2j49rZFpC1C7MGd7AzL0HnI14ZqT3tdfSCuE0iXNW6aYxI0Wi4BRvkmLvEuUThz6BMCS2B6ONtIiR"

        // Panduan
        var PANDUAN_URL = "https://ottopay.id/ottokasir/"

        // folder foto
        var FOLDER_APP = "OttoKasir"
        var FOLDER_FOTO = FOLDER_APP + File.separator + "Foto"

        // foto configuration
        const val REQUEST_CAMERA = 501
        const val REQUEST_GALLERY = 601
        var FILE_NAME_MAIN_FOTO = "foto"
        var EXTENSION_FILE_FOTO = ".jpg"
        var FILE_SEPARATOR = "_"

        // Receipt
        var FILE_NAME_MAIN_RECEIPT: String? = "Receipt"
        var FILE_NAME_FOTO_RECEIPT: String = FOLDER_FOTO + File.separator + FILE_NAME_MAIN_RECEIPT + FILE_SEPARATOR

        //QRIS
        var FILE_NAME_MAIN_QRIS: String? = "Qris"
        var FILE_NAME_FOTO_QRIS: String = FOLDER_FOTO + File.separator + FILE_NAME_MAIN_QRIS + FILE_SEPARATOR

        var FOLDER_PDF = FOLDER_APP + File.separator + "Pdf"
        var FILE_NAME_MAIN_PDF = "pdf"
        var EXTENSION_FILE_PDF = ".pdf"
        var FILE_NAME_PDF_RECEIPT: String = FOLDER_PDF + File.separator + FILE_NAME_MAIN_RECEIPT + FILE_SEPARATOR
        var SHARE_RECEIPT_TO: String? = "share_receipt_to"

        var KEY_BARCODE_RESULT = "result_barcode"
        var SCAN_BARCODE_ADD_PRODUCT = 101
        var SCAN_BARCODE_EDIT_PRODUCT = 102
        var SCAN_BARCODE_SEARCH_PRODUCT = 103

        var REQUEST_KASBON_QR = 112
        var REQUEST_PAYMENT_QR = 122

        var KEY_FIRST_TIME_ONBOARDING_KASBON = "first_time_onboarding_kasbon"
        var KEY_FIRST_TIME_ONBOARDING_MANAGE_PRODUCT = "first_time_onboarding_manage_product"


        // Customer
        var KEY_CUSTOMER_ID = "key_customer_id"
        var ADD_CHOOSE_CUSTOMER = 104

        var ISNT_FIRST_TNC_SHOW = "launcher_app"

        var REFERENCE_NUMBER = "reference_number"
        var ORDER_ID = "order_id"
        val IS_FROM_STORE_TYPE = "from_store_type"
        var PRODUCT_TYPE = "product_type"


        val SESSION_PIN = "pin"
        val SESSION_DEVICE_ID = "deviceddd"
        val NO_SECURE_LOGIN = "no_secure_login"
        val SESSION_LOGIN_KEY = "session_login"
        val SESSION_EMAIL = "email"
        val SESSION_PHONE = "phone"
        val SESSION_NAME = "name"
        val SESSION_ADDRESS = "address"
        val SESSION_MERCHANT_NAME = "sotere"
        val SESSION_BUSINESS_TYPE = "businiss"
        val SESSION_IMAGE = "imajes"
        val SESSION_MERCHANT_ID = "merchantID"
        val SESSION_CUSTOMER_CODE = "customerCode"
        val SESSION_WAREHOUSE_CODE = "warehouse"
        val SESSION_PAYMENT_CODE = "paymentCode"
        val SESSION_FINANCIAL_ACC_CODE = "financialCode"
        val SESSION_USER_ID = "userID"
        val SESSION_PROVINCE = "propince"
        val SESSION_IS_SYNC = "isSync"
        val SESSION_STORE_TYPE_ID = "storeTypeId"
        val SESSION_STORE_TYPE_NAME = "storeTypeName"
        val SESSION_NMID = "nmiddd"
        val SESSION_MID = "middmid"
        val SESSION_MPAN = "mpannn"

        val NO_FIRST_ENTER = "is_first_enter"

        val KEY_STOCKS = "key_stocks"
        val KEY_STOCKS_ALERT = "key_stocks_alert"
        val KEY_IS_STOCK_ACTIVE = "key_is_stock_active"
        val PRODUCT_ID_KEY = "productid"
        val PROFILE_DATA_KEY = "profileKey"
        val KEY_PAYMENT_HISTORY_ALL = "detailKey"
        val KEY_PAYMENT_CASH = "paymentCash"
        val KEY_PAYMENT_KASBON = "paymentCashBond"
        val KEY_PAYMENT_KASBON_CASH = "kasbon_cash_payment"
        val KEY_PAYMENT_QR = "paymentQr"
        val KEY_PAYMENT_KASBON_QR = "paymentQrKasbon"
        val PAYMENT_TYPE = "payment_type"
        val IS_FROM_OMZET = "isFromOmzet"
        val IS_ADD_PRODUCT = "isadddproduct"
        val PRODUCT_DATA = "porodukdata"

        val EDIT_PRODUCT_CODE = 119

        val ORDER_DETAIL_SERIALIZABLE = "order_detail_serialize"

        val monthLabels = arrayOf("January_Januari", "February_Februari", "March_Maret", "April_April", "May_Mei", "June_Juni", "July_Juli", "August_Agustus", "September_September", "October_Oktober", "November_November", "December_Desember")
    }
}
