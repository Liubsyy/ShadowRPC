package com.liubs.shadowrpc.research.grpc;

import com.liubs.shadowrpc.research.grpc.gen.GreeterGrpc;
import com.liubs.shadowrpc.research.grpc.gen.HelloReply;
import com.liubs.shadowrpc.research.grpc.gen.HelloRequest;
import io.grpc.stub.StreamObserver;

/**
 * @author Liubsyy
 * @date 2023/12/20
 */
public class GreeterImpl extends GreeterGrpc.GreeterImplBase {
    @Override
    public void sayHello(HelloRequest req, StreamObserver<HelloReply> responseObserver) {
        HelloReply reply = HelloReply.newBuilder().setMessage("Hello " + req.getName()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
