package com.citi.olympus.grpc.service;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GrpcServerMain {
    public static void main(String[] args) {
        try {
            Server server = ServerBuilder.forPort(9080)
                    .addService(new WriteServiceImpl())
                    .build();
            server.start();
            System.out.println("Server started at " + server.getPort());
            com.citi.olympus.grpc.service.DbConnect.bootstrapDB();
            server.awaitTermination();
        } catch (IOException e) {
            System.out.println("Error: " + e);
        } catch (InterruptedException e) {
            System.out.println("Error: " + e);
        }
    }
}
