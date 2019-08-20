package com.example.rishan.rishan.Services;

import com.example.rishan.rishan.Model.OrderItem;

import java.util.List;

public class OrderItemWrapper {

    List<OrderItem> orderItemList;

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }
}
