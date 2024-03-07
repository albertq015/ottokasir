package com.ottokonek.ottokasir.model.api.request;

import com.ottokonek.ottokasir.model.miscModel.OrderModel;

public class CreateOrderRequestModel {
    OrderModel order;

    public OrderModel getOrder() {
        return order;
    }

    public void setOrder(OrderModel order) {
        this.order = order;
    }
}
