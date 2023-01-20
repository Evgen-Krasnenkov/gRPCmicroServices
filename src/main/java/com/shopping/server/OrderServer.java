package com.shopping.server;

import com.shopping.service.OrderServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderServer {
    private static final Logger logger = Logger.getLogger(OrderServer.class.getName());
    private Server server;

    public void startServer(){
        int port = 50052;
        try {
            server = ServerBuilder.forPort(port)
                    .addService(new OrderServiceImpl())
                    .build()
                    .start();
            Runtime.getRuntime().addShutdownHook(new Thread(){
                public void run() {
                    OrderServer.this.stopServer();
                    logger.log(Level.SEVERE, "Server was stopped abruptly");
                }
            });
        logger.info("gRPC Order Server has been started successfully");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Problem to start gRPC server", e);
        }
    }
    public void stopServer(){
        if (server != null){
            try {
                server.awaitTermination(30, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public  void  stopRequestsShutWaitAndThenShutdown(){
        if(server != null) {
            try {
                server.awaitTermination();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void main(String[] args) {
        OrderServer orderServer = new OrderServer();
        orderServer.startServer();
        orderServer.stopRequestsShutWaitAndThenShutdown();
    }
}
