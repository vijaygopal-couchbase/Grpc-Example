package com.citi.olympus.grpc.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class GrpcConsoleRunner implements CommandLineRunner {

    @Autowired
    private GrpcClientService grpcClientService;
    @Override
    public void run(String... args) throws Exception {

        for(int i = 0; i < 5; i++) {
            grpcClientService.generateRequests();
        }
    }
}
