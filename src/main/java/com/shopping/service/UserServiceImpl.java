package com.shopping.service;

import com.shopping.grpcClient.GrpcOrderClient;
import com.shopping.model.User;
import com.shopping.dao.UserDao;
import com.shopping.stubs.order.Order;
import com.shopping.stubs.user.Gender;
import com.shopping.stubs.user.UserRequest;
import com.shopping.stubs.user.UserResponse;
import com.shopping.stubs.user.UserServiceGrpc.UserServiceImplBase;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserServiceImpl extends UserServiceImplBase {
    private static final Logger logger = Logger.getLogger(UserServiceImpl.class.getName());
    private UserDao userDao = new UserDao();
    @Override
    public void getUserDetails(UserRequest request, StreamObserver<UserResponse> responseObserver) {
        User userFromName = userDao.getUserFromName(request.getUsername());
        UserResponse.Builder userResponseBuilder = UserResponse.newBuilder()
                .setId(userFromName.getId())
                .setAge(userFromName.getAge())
                .setGender(Gender.valueOf(userFromName.getGender()))
                .setName(userFromName.getName())
                .setUsername(userFromName.getUsername());

        List<Order> ordersByUserId = getOrdersByUserId(userResponseBuilder.getId());
        userResponseBuilder.setNoOfOrders(ordersByUserId.size());
        UserResponse userResponse = userResponseBuilder.build();
        responseObserver.onNext(userResponse);
        responseObserver.onCompleted();
    }

    private List<Order> getOrdersByUserId(int userId) {
        ManagedChannel managedChannel = ManagedChannelBuilder
                .forTarget("localhost:50052")
                .usePlaintext()
                .build();
        GrpcOrderClient grpcOrderClient = new GrpcOrderClient(managedChannel);
        List<Order> ordersByUserId = grpcOrderClient.getOrderByUserId(userId);
        try {
            managedChannel.awaitTermination(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Problem with grpc channel", e);
        } return ordersByUserId;
    }
}
