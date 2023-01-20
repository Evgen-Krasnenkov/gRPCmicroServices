package com.shopping.service;

import com.google.protobuf.util.Timestamps;
import com.shopping.dao.OrderDao;
import com.shopping.model.Order;
import com.shopping.stubs.order.OrderRequest;
import com.shopping.stubs.order.OrderResponse;
import io.grpc.stub.StreamObserver;

import java.util.List;
import java.util.stream.Collectors;

import static com.shopping.stubs.order.OrderServiceGrpc.OrderServiceImplBase;

public class OrderServiceImpl extends OrderServiceImplBase {
    private OrderDao orderDao = new OrderDao();

    @Override
    public void getOrdersForUser(OrderRequest request, StreamObserver<OrderResponse> responseObserver) {
        List<Order> ordersByUserId = orderDao.getOrderByUserId(request.getUserId());
        List<com.shopping.stubs.order.Order> orders = ordersByUserId
                .stream()
                .map(order -> com.shopping.stubs.order.Order.newBuilder()
                        .setOrderId(order.getOrderId())
                        .setUserId(order.getUserId())
                        .setOrderDate(Timestamps.fromMillis(order.getOrderDate().getTime()))
                        .setNoOfTimes(order.getNoOfItems())
                        .setTotalAmount(order.getTotalAmount())
                        .build()
                )
                .collect(Collectors.toList());
        OrderResponse orderResponse = OrderResponse.newBuilder().addAllOrder(orders).build();
        responseObserver.onNext(orderResponse);
        responseObserver.onCompleted();
    }
}
