package com.citi.olympus.grpc.client;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@SpringBootApplication
@PropertySource("classpath:application.properties")
public class GrpcClientMain {
    public static void main(String args[]) throws InterruptedException {
        new SpringApplicationBuilder(GrpcClientMain.class)
                .logStartupInfo(false)
                .run();
    }
    @Bean(name = "asyncExecutor")
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("CITI-");
        executor.initialize();
        return executor;
    }
}
