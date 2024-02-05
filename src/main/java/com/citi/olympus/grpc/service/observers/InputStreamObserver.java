package com.citi.olympus.grpc.service.observers;

import com.citi.olympus.grpc.service.DbConnect;
import com.citi.olympus.grpc.stub.DBWriterService;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.kv.LookupInMacro;
import com.couchbase.client.java.kv.LookupInResult;
import com.couchbase.client.java.kv.LookupInSpec;
import io.grpc.stub.StreamObserver;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;

public class InputStreamObserver implements StreamObserver<DBWriterService.TradeDataRequest> {

    private final StreamObserver<DBWriterService.TradeDataResponse> outputStreamObserver;
    private final ConcurrentLinkedDeque<String> keyTracker = new ConcurrentLinkedDeque<>();
    private final Collection collection;

    public InputStreamObserver(StreamObserver<DBWriterService.TradeDataResponse> outputStreamObserver) {
        this.collection = DbConnect.getCollection();
        this.outputStreamObserver = outputStreamObserver;
    }

    @Override
    public void onNext(DBWriterService.TradeDataRequest tradeDataRequest) {
        String key = UUID.randomUUID().toString();
        System.out.println("Thread Name - "+Thread.currentThread().getName()+" :::: Key -- "+key+" -- Request created date time :: "+ tradeDataRequest.getReqTime());
        System.out.println("Thread Name - "+Thread.currentThread().getName()+" :::: Key -- "+key+" -- Request before sending date time :: "+ LocalDateTime.now().toString());
        keyTracker.push(key);
        JsonObject reqObj = JsonObject.create();
        reqObj.put("tradeDataReqTime",tradeDataRequest.getReqTime());
        collection.insert(key,reqObj);
        System.out.println("Thread Name - "+Thread.currentThread().getName()+" :::: Key -- "+key+" -- Post persisting to database :: "+ LocalDateTime.now().toString());
    }

    @Override
    public void onError(Throwable throwable) {
        //System.out.println("InputStreamReader.onError invoked ");
    }

    @Override
    public void onCompleted() {

       String key = keyTracker.pop();
        LookupInResult result = collection.lookupIn(key,Arrays.asList(
                LookupInSpec.get(LookupInMacro.LAST_MODIFIED).xattr()));

        DBWriterService.TradeDataResponse response = DBWriterService.TradeDataResponse.newBuilder()
                    .setRespTime(
                            LocalDateTime.ofInstant(
                                    Instant.ofEpochSecond(
                                            result.contentAs(0,Integer.class)
                                    ), ZoneId.systemDefault()).toString()
                            )
                    .setKey(key)
                    .build();
        outputStreamObserver.onNext(response);
        outputStreamObserver.onCompleted();
    }
}
