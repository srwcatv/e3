package com.catv.order.service;


import com.catv.common.pojo.E3Result;
import com.catv.order.pojo.OrderInfo;

public interface OrderService {

	E3Result createOrder(OrderInfo orderInfo);
}
