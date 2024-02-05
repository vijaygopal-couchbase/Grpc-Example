package com.citi.olympus.grpc.client;

import com.citi.olympus.grpc.service.observers.OutputStreamObserver;
import com.citi.olympus.grpc.stub.DBWriterService;
import com.citi.olympus.grpc.stub.WriteServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class GrpcClientService {

    @Async("grpcThreadExecutor")
    public void generateRequests(){
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",9080)
                .usePlaintext().build();
        WriteServiceGrpc.WriteServiceStub writeStub = WriteServiceGrpc.newStub(channel);
        System.out.println("Invoked  GrpcClientService.generateRequests by Thread :: "+ Thread.currentThread().getName());
        for(int i=0;i<=1000 ; i++){
            StreamObserver<DBWriterService.TradeDataRequest> inputStreamObsrvr =
                    writeStub.validateTradeData(new OutputStreamObserver());
            LocalDateTime requestSentTime = LocalDateTime.now(ZoneId.systemDefault());
            DBWriterService.TradeDataRequest dataRequest = DBWriterService.TradeDataRequest.newBuilder()
                    .setReqTime(requestSentTime.toString())
                    .build();
            inputStreamObsrvr.onNext(dataRequest);
            inputStreamObsrvr.onCompleted();
        }
    }
}
