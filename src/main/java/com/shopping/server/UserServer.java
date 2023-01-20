package com.shopping.server;

import com.shopping.service.UserServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserServer {
   private static final Logger logger = Logger.getLogger(UserServer.class.getName());
   private Server server;

   public void startServer(){
       int port = 50051;
       try {
          server = ServerBuilder
                   .forPort(port)
                   .addService( new UserServiceImpl())
                   .build()
                   .start();
          logger.info("Server has started");
          Runtime.getRuntime().addShutdownHook(new Thread(){
                  public void run() {
                      UserServer.this.stopServer();
                      logger.log(Level.SEVERE, "Server was stopped abruptly");
                  }
          });

       } catch (IOException e) {
           throw new RuntimeException(e);
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
        UserServer userServer = new UserServer();
        userServer.startServer();
        userServer.stopRequestsShutWaitAndThenShutdown();
    }
}
