package com.shopping.grpcClient;

import com.shopping.stubs.order.Order;
import com.shopping.stubs.order.OrderRequest;
import com.shopping.stubs.order.OrderResponse;
import com.shopping.stubs.order.OrderServiceGrpc;
import com.shopping.stubs.order.OrderServiceGrpc.OrderServiceBlockingStub;
import io.grpc.Channel;

import java.util.List;

public class GrpcOrderClient {
    private OrderServiceBlockingStub orderServiceBlockingStub;

    public GrpcOrderClient(Channel channel) {
        orderServiceBlockingStub = OrderServiceGrpc.newBlockingStub(channel);
    }
    public List<Order> getOrderByUserId(int id){
        OrderRequest orderRequest = OrderRequest.newBuilder().setUserId(id).build();
        OrderResponse orderResponse = orderServiceBlockingStub.getOrdersForUser(orderRequest);
        return orderResponse.getOrderList();
    }
}
