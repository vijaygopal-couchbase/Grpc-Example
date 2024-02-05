package com.citi.olympus.grpc.service.observers;

import com.citi.olympus.grpc.stub.DBWriterService;
import io.grpc.stub.StreamObserver;

public class OutputStreamObserver implements StreamObserver<DBWriterService.TradeDataResponse> {
    @Override
    public void onNext(DBWriterService.TradeDataResponse tradeDataResponse) {
        System.out.println("Thread Name - "+Thread.currentThread().getName()+" ::::  Key -- "+tradeDataResponse.getKey()+" record saved in database datetime :: " + tradeDataResponse.getRespTime());
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("OutputStreamObserver onError invoked: ");
    }

    @Override
    public void onCompleted() {
        System.out.println("OutputStreamObserver onCompleted invoked: ");
    }
}
