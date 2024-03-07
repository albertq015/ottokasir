package com.ottokonek.ottokasir.model.api

import android.content.Context
import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BaseApi
import app.beelabs.com.codebase.base.response.BaseResponse
import app.beelabs.com.codebase.support.util.CacheUtil
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.ottokonek.ottokasir.App
import com.ottokonek.ottokasir.BuildConfig
import com.ottokonek.ottokasir.IConfig.Companion.SESSION_DEVICE_ID
import com.ottokonek.ottokasir.model.api.request.*
import com.ottokonek.ottokasir.model.api.response.*
import io.reactivex.Observable
import okhttp3.Interceptor


class Api : BaseApi() {

    companion object {

        private fun initHeader(): Map<String, String> {
            val map = HashMap<String, String>()
            map["Content-Type"] = "application/json"
            map["Language-Id"] = "en"
            map["Device-Id"] = CacheUtil.getPreferenceString(SESSION_DEVICE_ID, App.context)
            map["Authorization"] = "Bearer " + com.ottokonek.ottokasir.presenter.manager.SessionManager.getCredential(App.context)

            return map
        }

        private fun initHeaderLogin(activity: BaseActivity): Map<String, String> {
            val map = HashMap<String, String>()
            map["Content-Type"] = "application/json"
            map["Language-Id"] = "eng"
            map["Device-Id"] = CacheUtil.getPreferenceString(SESSION_DEVICE_ID, App.context)

            return map
        }


        @Synchronized
        private fun initApiDomain(baseURL: String): ApiService {
            val timeout = 60

            if (BuildConfig.DEBUG) {
                val interceptors = Array<Interceptor?>(1) { App.context?.let { ChuckerInterceptor(it) } }
                return BaseApi.getInstance().setupApiDomain(baseURL, App.getAppComponent(), ApiService::class.java, true, timeout, true, interceptors) as ApiService
            }
            return BaseApi.getInstance().setupApiDomain(baseURL, App.getAppComponent(), ApiService::class.java, true, timeout, false) as ApiService
        }

        @Synchronized
        fun onLogin(loginRequestModel: LoginRequestModel, activity: BaseActivity): Observable<LoginResponseModel> {
            return initApiDomain(BuildConfig.SERVER_URL_OFIN).callApiLogin(initHeaderLogin(activity), loginRequestModel)
        }

        @Synchronized
        fun onLogout(): Observable<LogoutResponse> {
            return initApiDomain(BuildConfig.SERVER_URL_OFIN).callLogout(initHeader())
        }

        @Synchronized
        fun onAvailableOmzet(activity: BaseActivity): Observable<AvailableOmzetResponseModel> {
            return initApiDomain(BuildConfig.SERVER_URL_OK_3_0).callAvailableOmzet(initHeader())
        }

        @Synchronized
        fun onTransactionHistory(model: HistoryRequestModel): Observable<TransactionHistoryResponse> {
            return initApiDomain(BuildConfig.SERVER_URL_OK_3_0).callHistoryTransaksi(initHeader(), model)
        }

        @Synchronized
        fun doGetProductlist(page: String?, query: String?, model: ProductListRequestModel): Observable<ProductlistResponseModel> {
            val header = initHeader()
            return initApiDomain(BuildConfig.SERVER_URL_OK_3_1).callProductList(header, page, query, model)
        }

        @Synchronized
        fun onPaymentCash(model: PaymentCashRequestModel): Observable<PaymentCashResponseModel> {
            return initApiDomain(BuildConfig.SERVER_URL_OK_3_1).callPaymentCash(initHeader(), model)
        }

        @Synchronized
        fun onPaymentCashBond(model: PaymentCashBondRequestModel): Observable<PaymentCashBondResponseModel> {
            return initApiDomain(BuildConfig.SERVER_URL_OK_3_1).callPaymentCashBond(initHeader(), model)
        }

        @Synchronized
        fun onQrPaymentGenerate(model: QrPaymentGenerateRequestModel, activity: BaseActivity): Observable<PaymentQrResponseModel> {
            return initApiDomain(BuildConfig.SERVER_URL_OK_3_1).callGenerateQR(initHeader(), model)
        }

        @Synchronized
        fun onQrPaymentCheckStatus(model: QrPaymentCheckStatRequestModel, activity: BaseActivity): Observable<CheckStatusQrResponseModel> {
            return initApiDomain(BuildConfig.SERVER_URL_OK_3_1).callCheckStatusQR(initHeader(), model)
        }

        @Synchronized
        fun onProfile(): Observable<ProfileResponseModel> {
            return initApiDomain(BuildConfig.SERVER_URL_OK_3_0).callProfile(initHeader())
        }

        @Synchronized
        fun doGetTransactionReport(model: HistoryRequestModel, activity: BaseActivity): Observable<TransactionReportResponse> {
            return initApiDomain(BuildConfig.SERVER_URL_OK_3_0).callTransactionReport(initHeader(), model)
        }

        @Synchronized
        fun onExportTransaction(data: ExportTransactionRequestModel, activity: BaseActivity): Observable<TransactionExportResponseModel> {
            return initApiDomain(BuildConfig.SERVER_URL_OK_3_0).callExportTransaction(initHeader(), data)
        }

        @Synchronized
        fun doLoginSync(data: LoginSyncRequest, activity: BaseActivity): Observable<LoginResponseModel> {
            return initApiDomain(BuildConfig.SERVER_URL_OK_3_0).callLoginSync(initHeader(), data)
        }

        @Synchronized
        fun onUpdateProduct(data: UpdateProductRequestModel, activity: BaseActivity): Observable<UpdateProductResponse> {
            return initApiDomain(BuildConfig.SERVER_URL_OK_3_1).updateProduct(initHeader(), data)
        }

        @Synchronized
        fun onDeleteProduct(data: DeleteProductRequestModel, activity: BaseActivity): Observable<DeleteProductResponse> {
            return initApiDomain(BuildConfig.SERVER_URL_OK_3_0).deleteProduct(initHeader(), data)
        }

        @Synchronized
        fun onCreateProduct(data: CreateProductRequestModel, activity: BaseActivity): Observable<CreateProductResponse> {
            return initApiDomain(BuildConfig.SERVER_URL_OK_3_1).createProduct(initHeader(), data)
        }

        @Synchronized
        fun onGetStoreTypeList(data: StoreTypeListRequestModel, context: Context): Observable<StoreTypeListResponseModel> {
            return initApiDomain(BuildConfig.SERVER_URL_OK_3_0).callStoreTypeList(initHeader(), data)
        }

        @Synchronized
        fun onGetStoreTypeProduct(data: StoreTypeProductRequestModel, context: Context): Observable<StoreTypeProductResponseModel> {
            return initApiDomain(BuildConfig.SERVER_URL_OK_3_0).callStoreTypeProduct(initHeader(), data)
        }

        @Synchronized
        fun onGetStoreTypeProductAdd(data: StoreTypeProductAddRequestModel, context: Context): Observable<StoreTypeProductAddResponseModel> {
            return initApiDomain(BuildConfig.SERVER_URL_OK_3_1).callStoreTypeProductAdd(initHeader(), data)
        }

        @Synchronized
        fun onMasterProduct(data: MasterProductRequestModel, activity: BaseActivity): Observable<MasterProductResponseModel> {
            return initApiDomain(BuildConfig.SERVER_URL_OK_3_1).callMasterProduct(initHeader(), data)
        }

        @Synchronized
        fun onRefund(data: RefundRequestModel, activity: BaseActivity): Observable<RefundResponseModel> {
            return initApiDomain(BuildConfig.SERVER_URL_OK_3_0).callRefund(initHeader(), data)
        }

        @Synchronized
        fun onCheckRefund(data: CheckRefundRequestModel, activity: BaseActivity): Observable<CheckRefundResponseModel> {
            return initApiDomain(BuildConfig.SERVER_URL_OK_3_0).callCheckRefund(initHeader(), data)
        }

        @Synchronized
        fun onResetStoreType(data: ResetStoreTypeRequestModel, activity: BaseActivity): Observable<ResetStoreTypeResponseModel> {
            return initApiDomain(BuildConfig.SERVER_URL_OK_3_0).callResetStoreType(initHeader(), data)
        }

        @Synchronized
        fun onCustomerList(data: CustomerListRequestModel, activity: BaseActivity): Observable<CustomerListResponseModel> {
            return initApiDomain(BuildConfig.SERVER_URL_OK_3_0).callCustomerList(initHeader(), data)
        }

        @Synchronized
        fun onCustomerCreate(data: CustomerCreateRequestModel, activity: BaseActivity): Observable<CustomerCreateResponseModel> {
            return initApiDomain(BuildConfig.SERVER_URL_OK_3_0).callCustomerCreate(initHeader(), data)
        }

        @Synchronized
        fun onCustomerDetail(data: CustomerDetailRequestModel, activity: BaseActivity): Observable<CustomerDetailResponseModel> {
            return initApiDomain(BuildConfig.SERVER_URL_OK_3_0).callCustomerDetail(initHeader(), data)
        }

        @Synchronized
        fun onCustomerDelete(data: CustomerDeleteRequestModel, activity: BaseActivity): Observable<BaseResponse> {
            return initApiDomain(BuildConfig.SERVER_URL_OK_3_0).callCustomerDelete(initHeader(), data)
        }

        @Synchronized
        fun onCustomerUpdate(data: CustomerUpdateRequestModel, activity: BaseActivity): Observable<CustomerUpdateResponseModel> {
            return initApiDomain(BuildConfig.SERVER_URL_OK_3_0).callCustomerUpdate(initHeader(), data)
        }

        @Synchronized
        fun onCustomerHistory(data: CustomerHistoryRequestModel, activity: BaseActivity): Observable<TransactionHistoryResponse> {
            return initApiDomain(BuildConfig.SERVER_URL_OK_3_0).callCustomerHistory(initHeader(), data)
        }

        @Synchronized
        fun onCustomerKasbon(data: CustomerKasbonRequestModel, activity: BaseActivity): Observable<CustomerKasbonResponseModel> {
            return initApiDomain(BuildConfig.SERVER_URL_OK_3_1).callCustomerKasbon(initHeader(), data)
        }


        @Synchronized
        fun onKasbonAktif(data: KasbonAktifRequestModel, activity: BaseActivity): Observable<KasbonAktifResponseModel> {
            return initApiDomain(BuildConfig.SERVER_URL_OK_3_0).callKasbonAktif(initHeader(), data)
        }

        @Synchronized
        fun onKasbonLunas(data: KasbonLunasRequestModel, activity: BaseActivity): Observable<KasbonLunasResponseModel> {
            return initApiDomain(BuildConfig.SERVER_URL_OK_3_0).callKasbonLunas(initHeader(), data)
        }

        @Synchronized
        fun onKasbonCustomer(data: KasbonCustomerRequestModel, activity: BaseActivity): Observable<KasbonCustomerResponseModel> {
            return initApiDomain(BuildConfig.SERVER_URL_OK_3_0).callKasbonCustomer(initHeader(), data)
        }

        @Synchronized
        fun onKasbonCustomerDetail(data: KasbonCustomerDetailRequestModel, activity: BaseActivity): Observable<KasbonAktifSelectedResponseModel> {
            return initApiDomain(BuildConfig.SERVER_URL_OK_3_0).callKasbonCustomerDetail(initHeader(), data)
        }

        @Synchronized
        fun onKasbonReport(data: KasbonReportRequestModel, activity: BaseActivity): Observable<KasbonReportResponseModel> {
            return initApiDomain(BuildConfig.SERVER_URL_OK_3_0).callKasbonReport(initHeader(), data)
        }

        @Synchronized
        fun onKasbonExport(data: KasbonExportRequestModel, activity: BaseActivity): Observable<KasbonExportResponseModel> {
            return initApiDomain(BuildConfig.SERVER_URL_OK_3_0).callKasbonExport(initHeader(), data)
        }

        @Synchronized
        fun onKasbonAktifSelected(data: KasbonAktifSelectedRequestModel, activity: BaseActivity): Observable<KasbonAktifSelectedResponseModel> {
            return initApiDomain(BuildConfig.SERVER_URL_OK_3_0).callKasbonAktifSelected(initHeader(), data)
        }

        @Synchronized
        fun onKasbonCashPayment(data: KasbonCashPaymentRequestModel, activity: BaseActivity): Observable<KasbonCashPaymentResponseModel> {
            return initApiDomain(BuildConfig.SERVER_URL_OK_3_0).callKasbonCashPayment(initHeader(), data)
        }

        @Synchronized
        fun onKasbonQrPaymentGenerate(data: KasbonQrPaymentRequestModel, activity: BaseActivity): Observable<KasbonQrPaymentResponseModel> {
            return initApiDomain(BuildConfig.SERVER_URL_OK_3_1).callKasbonQrPaymentGenerate(initHeader(), data)
        }

        @Synchronized
        fun onKasbonQrPaymentStatus(data: KasbonQrPaymentStatusRequestModel, activity: BaseActivity): Observable<KasbonQrPaymentStatusResponseModel> {
            return initApiDomain(BuildConfig.SERVER_URL_OK_3_1).callKasbonQrPaymentStatus(initHeader(), data)
        }

        @Synchronized
        fun onCheckVersion(data: CheckVersionRequestModel, activity: BaseActivity): Observable<CheckVersionResponseModel> {
            return initApiDomain(BuildConfig.SERVER_URL_OK_3_1).callCheckVersion(initHeader(), data)
        }
    }
}