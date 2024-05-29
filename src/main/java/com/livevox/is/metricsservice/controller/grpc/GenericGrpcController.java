package com.livevox.is.metricsservice.controller.grpc;

import com.livevox.is.metricsservice.service.GenericService;
import com.livevox.is.metricsservice.service.impl.GenericServiceImpl;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.Observer;

@Slf4j
//@GrpcService
public class GenericGrpcController {

    public void getGenericResponse(StreamObserver<String> observer){
        observer.onNext("Hello World");
        observer.onCompleted();
    }

}
