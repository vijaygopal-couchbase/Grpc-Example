package com.citi.olympus.grpc.service;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.ClusterOptions;
import com.couchbase.client.java.Collection;

import java.time.Duration;


public class DbConnect {

    private static Collection collection;
    private static String connectionString =
            "couchbase://ec2-3-139-37-235.us-east-2.compute.amazonaws.com," +
            "ec2-18-220-113-42.us-east-2.compute.amazonaws.com," +
            "ec2-18-117-175-85.us-east-2.compute.amazonaws.com";
    private static String username = "Administrator";
    private static String password = "password";
    private static String bucketName = "Test";

    private  DbConnect(){
    }

    static Collection bootstrapDB(){
        if(collection!=null){
            return collection;
        }else{
            Cluster cluster = Cluster.connect(
                    connectionString,
                    ClusterOptions.clusterOptions(username, password).environment(env -> {
                    })
            );
            // Get a bucket reference
            Bucket bucket = cluster.bucket(bucketName);
            bucket.waitUntilReady(Duration.ofSeconds(20));
            collection = bucket.defaultCollection();
            return collection;
        }
    }
    public static Collection getCollection(){
        return collection;
    }
}
