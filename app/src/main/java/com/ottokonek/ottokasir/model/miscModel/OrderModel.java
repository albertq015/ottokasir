package com.ottokonek.ottokasir.model.miscModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderModel {

    @JsonProperty("total_paid")
    private String totalPaid;
    @JsonProperty("total_amount")
    private String totalAmount;
    @JsonProperty("store_discount")
    private String storeDiscount;
    @JsonProperty("paid_nominal")
    private String paidNominal;
    String change;
    @JsonProperty("invoices_attributes")
    private List<OrderInvoiceAttributeModel> invoicesAttributes;
    @JsonProperty("payment_method")
    private String paymentMethod;

    public String getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(String totalPaid) {
        this.totalPaid = totalPaid;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStoreDiscount() {
        return storeDiscount;
    }

    public void setStoreDiscount(String storeDiscount) {
        this.storeDiscount = storeDiscount;
    }

    public String getPaidNominal() {
        return paidNominal;
    }

    public void setPaidNominal(String paidNominal) {
        this.paidNominal = paidNominal;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public List<OrderInvoiceAttributeModel> getInvoicesAttributes() {
        return invoicesAttributes;
    }

    public void setInvoicesAttributes(List<OrderInvoiceAttributeModel> invoicesAttributes) {
        this.invoicesAttributes = invoicesAttributes;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
