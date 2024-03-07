package com.ottokonek.ottokasir.model.api


import app.beelabs.com.codebase.base.response.BaseResponse
import com.ottokonek.ottokasir.model.api.request.*
import com.ottokonek.ottokasir.model.api.response.*
import io.reactivex.Observable
import retrofit2.http.*


interface ApiService {

    @POST("ottocashier/login")
    fun callApiLogin(@HeaderMap headers: Map<String, String>, @Body loginRequestModel: LoginRequestModel): Observable<LoginResponseModel>

    @POST("ottocashier/logout")
    fun callLogout(@HeaderMap headers: Map<String, String>): Observable<LogoutResponse>

    @GET("users/profile")
    fun callProfile(@HeaderMap headers: Map<String, String>): Observable<ProfileResponseModel>


    @POST("product/list")
    fun callProductList(@HeaderMap headers: Map<String, String>,
                        @Query("page") page: String?,
                        @Query("q") query: String?,
                        @Body productListRequest: ProductListRequestModel): Observable<ProductlistResponseModel>

    @POST("order/qr/check")
    fun callCheckStatusQR(@HeaderMap headers: Map<String, String>,
                          @Body model: QrPaymentCheckStatRequestModel): Observable<CheckStatusQrResponseModel>

    @POST("order/qr/generate")
    fun callGenerateQR(@HeaderMap headers: Map<String, String>,
                       @Body model: QrPaymentGenerateRequestModel): Observable<PaymentQrResponseModel>

    @POST("order/add")
    fun callPaymentCash(@HeaderMap headers: Map<String, String>,
                        @Body model: PaymentCashRequestModel): Observable<PaymentCashResponseModel>

    @POST("order/add")
    fun callPaymentCashBond(@HeaderMap headers: Map<String, String>,
                            @Body model: PaymentCashBondRequestModel): Observable<PaymentCashBondResponseModel>

    @POST("account/omzet")
    fun callAvailableOmzet(@HeaderMap headers: Map<String, String>): Observable<AvailableOmzetResponseModel>

    @POST("order/history/summary")
    fun callTransactionReport(@HeaderMap headers: Map<String, String>,
                              @Body body: HistoryRequestModel): Observable<TransactionReportResponse>

    @POST("order/history/summary/export")
    fun callExportTransaction(@HeaderMap headers: Map<String, String>,
                              @Body body: ExportTransactionRequestModel): Observable<TransactionExportResponseModel>

    @POST("order/history")
    fun callHistoryTransaksi(@HeaderMap headers: Map<String, String>,
                             @Body body: HistoryRequestModel): Observable<TransactionHistoryResponse>

    @POST("account/sync")
    fun callLoginSync(@HeaderMap headers: Map<String, String>,
                      @Body body: LoginSyncRequest): Observable<LoginResponseModel>

    @POST("product/update")
    fun updateProduct(@HeaderMap headers: Map<String, String>,
                      @Body body: UpdateProductRequestModel): Observable<UpdateProductResponse>

    @POST("product/create")
    fun createProduct(@HeaderMap headers: Map<String, String>,
                      @Body body: CreateProductRequestModel): Observable<CreateProductResponse>

    @POST("product/delete")
    fun deleteProduct(@HeaderMap headers: Map<String, String>,
                      @Body body: DeleteProductRequestModel): Observable<DeleteProductResponse>


    @POST("store-type/list")
    fun callStoreTypeList(@HeaderMap headers: Map<String, String>,
                          @Body body: StoreTypeListRequestModel): Observable<StoreTypeListResponseModel>

    @POST("store-type/products")
    fun callStoreTypeProduct(@HeaderMap headers: Map<String, String>,
                             @Body body: StoreTypeProductRequestModel): Observable<StoreTypeProductResponseModel>

    @POST("store-type/products/add")
    fun callStoreTypeProductAdd(@HeaderMap headers: Map<String, String>,
                                @Body body: StoreTypeProductAddRequestModel): Observable<StoreTypeProductAddResponseModel>

    @POST("master-product/list")
    fun callMasterProduct(@HeaderMap headers: Map<String, String>,
                          @Body body: MasterProductRequestModel): Observable<MasterProductResponseModel>

    @POST("account/reset")
    fun callResetStoreType(@HeaderMap headers: Map<String, String>,
                           @Body body: ResetStoreTypeRequestModel): Observable<ResetStoreTypeResponseModel>


    @POST("order/qr/refund")
    fun callRefund(@HeaderMap headers: Map<String, String>,
                   @Body body: RefundRequestModel): Observable<RefundResponseModel>

    @POST("order/qr/refund/check")
    fun callCheckRefund(@HeaderMap headers: Map<String, String>,
                        @Body body: CheckRefundRequestModel): Observable<CheckRefundResponseModel>


    @POST("merchant-customer/list")
    fun callCustomerList(@HeaderMap headers: Map<String, String>,
                         @Body body: CustomerListRequestModel): Observable<CustomerListResponseModel>

    @POST("merchant-customer/create")
    fun callCustomerCreate(@HeaderMap headers: Map<String, String>,
                           @Body body: CustomerCreateRequestModel): Observable<CustomerCreateResponseModel>

    @POST("merchant-customer/detail")
    fun callCustomerDetail(@HeaderMap headers: Map<String, String>,
                           @Body body: CustomerDetailRequestModel): Observable<CustomerDetailResponseModel>

    @POST("merchant-customer/delete")
    fun callCustomerDelete(@HeaderMap headers: Map<String, String>,
                           @Body body: CustomerDeleteRequestModel): Observable<BaseResponse>

    @POST("merchant-customer/update")
    fun callCustomerUpdate(@HeaderMap headers: Map<String, String>,
                           @Body body: CustomerUpdateRequestModel): Observable<CustomerUpdateResponseModel>

    @POST("merchant-customer/history")
    fun callCustomerHistory(@HeaderMap headers: Map<String, String>,
                            @Body body: CustomerHistoryRequestModel): Observable<TransactionHistoryResponse>

    @POST("merchant-customer/list-cashbond")
    fun callCustomerKasbon(@HeaderMap headers: Map<String, String>,
                           @Body body: CustomerKasbonRequestModel): Observable<CustomerKasbonResponseModel>


    @POST("cashbond/type")
    fun callKasbonAktif(@HeaderMap headers: Map<String, String>,
                        @Body body: KasbonAktifRequestModel): Observable<KasbonAktifResponseModel>

    @POST("cashbond/type")
    fun callKasbonLunas(@HeaderMap headers: Map<String, String>,
                        @Body body: KasbonLunasRequestModel): Observable<KasbonLunasResponseModel>

    @POST("cashbond/customer/list")
    fun callKasbonCustomer(@HeaderMap headers: Map<String, String>,
                           @Body body: KasbonCustomerRequestModel): Observable<KasbonCustomerResponseModel>

    @POST("cashbond/customer/detail")
    fun callKasbonCustomerDetail(@HeaderMap headers: Map<String, String>,
                                 @Body body: KasbonCustomerDetailRequestModel): Observable<KasbonAktifSelectedResponseModel>

    @POST("cashbond/report")
    fun callKasbonReport(@HeaderMap headers: Map<String, String>,
                         @Body body: KasbonReportRequestModel): Observable<KasbonReportResponseModel>

    @POST("cashbond/report/export")
    fun callKasbonExport(@HeaderMap headers: Map<String, String>,
                         @Body body: KasbonExportRequestModel): Observable<KasbonExportResponseModel>

    @POST("cashbond/bulk")
    fun callKasbonAktifSelected(@HeaderMap headers: Map<String, String>,
                                @Body body: KasbonAktifSelectedRequestModel): Observable<KasbonAktifSelectedResponseModel>


    @POST("cashbond/cash-payment")
    fun callKasbonCashPayment(@HeaderMap headers: Map<String, String>,
                              @Body body: KasbonCashPaymentRequestModel): Observable<KasbonCashPaymentResponseModel>

    @POST("cashbond/qr/generate")
    fun callKasbonQrPaymentGenerate(@HeaderMap headers: Map<String, String>,
                                    @Body body: KasbonQrPaymentRequestModel): Observable<KasbonQrPaymentResponseModel>

    @POST("order/qr/check")
    fun callKasbonQrPaymentStatus(@HeaderMap headers: Map<String, String>,
                                  @Body body: KasbonQrPaymentStatusRequestModel): Observable<KasbonQrPaymentStatusResponseModel>

    @POST("check-version")
    fun callCheckVersion(@HeaderMap headers: Map<String, String>,
                         @Body body: CheckVersionRequestModel): Observable<CheckVersionResponseModel>

}