package com.citi.olympus.grpc.service;

import com.citi.olympus.grpc.service.observers.InputStreamObserver;
import com.citi.olympus.grpc.stub.DBWriterService;
import com.citi.olympus.grpc.stub.WriteServiceGrpc;
import io.grpc.stub.StreamObserver;

public class WriteServiceImpl extends WriteServiceGrpc.WriteServiceImplBase {

    @Override
    public StreamObserver<DBWriterService.TradeDataRequest> validateTradeData(StreamObserver<DBWriterService.TradeDataResponse> responseObserver) {
        return new InputStreamObserver(responseObserver);
    }
}
